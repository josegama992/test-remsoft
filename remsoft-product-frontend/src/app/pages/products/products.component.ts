import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, inject, OnInit, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { TableLazyLoadEvent, TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { finalize } from 'rxjs';
import { ProductsService } from '../../../core/services/products.service';
import { Product, ProductForm } from '../../../core/types/products.types';

@Component({
  selector: 'app-messages-demo',
  standalone: true,
  imports: [CommonModule, TableModule, ToastModule, ButtonModule, DialogModule, CurrencyPipe, DividerModule, ReactiveFormsModule, InputTextModule, FloatLabelModule, InputNumberModule],
  templateUrl: './products.component.html',
  providers: [MessageService],
  styles: `
    ::ng-deep.p-dialog-header {
      padding-bottom: 0 !important;
    }
  `,
})
export class ProductsComponent implements OnInit {
  service = inject(ProductsService);
  messageService = inject(MessageService);
  nonNullableFormBuilder = inject(NonNullableFormBuilder);

  products = signal<Product[]>([]);
  id = signal<number | undefined>(undefined);
  loading = signal(false);
  totalRecords = signal(0);

  confirmationDialog = false;
  saveDialog = false;

  formGroup!: ProductForm;

  ngOnInit(): void {
    this.loadProducts();
    this.initializeForm();
  }

  initializeForm() {
    this.formGroup = this.nonNullableFormBuilder.group({
      nome: ['', Validators.required],
      valor: ['', Validators.required],
    });
  }

  loadProducts(event?: TableLazyLoadEvent) {
    this.loading.set(true);

    const params = {
      page: 0,
      size: 10,
    };

    if (event?.first != undefined && event?.rows != undefined) {
      params.page = event.first / event.rows;
      params.size = event.rows;
    }

    this.service
      .load(params.page, params.size)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.totalRecords.set(response.totalElements);
        this.products.set(response.content);
        console.log(this.totalRecords());
      });
  }

  hideConfirmDialog() {
    this.id.set(undefined);
    this.confirmationDialog = false;
  }

  hideSaveDialog() {
    this.id.set(undefined);
    this.formGroup.reset();
    this.saveDialog = false;
  }

  showConfirmDialog(id: number) {
    this.id.set(id);
    this.confirmationDialog = true;
  }

  showSaveDialog(id?: number) {
    if (id) {
      const product = this.products().find((p) => p.id == id);
      if (product) {
        const dto = { nome: product.nome, valor: product.valor.toString() };
        this.formGroup.patchValue(dto);
      }
      this.id.set(id);
    }

    this.saveDialog = true;
  }

  handleSave() {
    this.loading.set(true);
    this.service
      .save(this.formGroup?.value as any, this.id())
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: `Produto ${this.id() ? 'editado' : 'criado'} com sucesso!` });
          this.loadProducts();
          this.hideSaveDialog();
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: error.error.userMessage });
        },
      });
  }

  handleDelete() {
    this.loading.set(false);
    this.service
      .delete(this.id()!)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe({
        next: () => {
          this.messageService.add({ severity: 'success', summary: 'Produto deletado com sucesso!' });
          this.loadProducts();
          this.hideConfirmDialog();
        },
        error: (error) => {
          this.messageService.add({ severity: 'error', summary: error.error.userMessage });
        },
      });
  }

  get controls() {
    return this.formGroup.controls;
  }
}
