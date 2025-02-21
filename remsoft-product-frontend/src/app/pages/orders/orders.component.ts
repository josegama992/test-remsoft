import { CommonModule, CurrencyPipe } from '@angular/common';
import { Component, DestroyRef, inject, signal } from '@angular/core';
import { FormsModule, NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MessageService } from 'primeng/api';
import { ButtonModule } from 'primeng/button';
import { DialogModule } from 'primeng/dialog';
import { DividerModule } from 'primeng/divider';
import { FloatLabelModule } from 'primeng/floatlabel';
import { InputNumberModule } from 'primeng/inputnumber';
import { InputTextModule } from 'primeng/inputtext';
import { MessageModule } from 'primeng/message';
import { SelectModule } from 'primeng/select';
import { TableModule } from 'primeng/table';
import { ToastModule } from 'primeng/toast';
import { OrdersService } from '../../../core/services/orders.service';
import { ProductsService } from '../../../core/services/products.service';
import { Order, OrderForm, OrderProductForm } from '../../../core/types/orders.types';
import { Product } from '../../../core/types/products.types';

import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { finalize } from 'rxjs';

@Component({
  selector: 'app-messages-demo',
  standalone: true,
  imports: [CommonModule, FormsModule, TableModule, ToastModule, MessageModule, ButtonModule, DialogModule, CurrencyPipe, DividerModule, ReactiveFormsModule, FloatLabelModule, InputTextModule, InputNumberModule, SelectModule, FormsModule],
  templateUrl: './orders.component.html',
  providers: [MessageService],
  styles: `
    ::ng-deep.p-dialog-header {
      padding-bottom: 0 !important;
    }
  `,
})
export class OrdersComponent {
  service = inject(OrdersService);
  productService = inject(ProductsService);
  messageService = inject(MessageService);
  nonNullableFormBuilder = inject(NonNullableFormBuilder);
  destroyRef = inject(DestroyRef);

  orders = signal<Order[]>([]);
  products = signal<Product[]>([]);
  id = signal<number | undefined>(undefined);
  loading = signal(false);

  confirmationDialog = false;
  saveDialog = false;

  formGroup!: OrderForm;

  ngOnInit(): void {
    this.loadProducts();
    this.loadOrders();
    this.initializeForm();
    this.handleProductTotalChanges();
  }

  initializeForm() {
    this.formGroup = this.nonNullableFormBuilder.group({
      nomeComprador: [''],
      nomeForncedor: [''],
      valorTotal: [''],
      produtos: this.nonNullableFormBuilder.array<OrderProductForm>([], Validators.required),
    });
  }

  handleProductTotalChanges() {
    this.formGroup.valueChanges.pipe(takeUntilDestroyed(this.destroyRef)).subscribe((formValue) => {
      let totalValue = 0;
      if (formValue.produtos?.length) {
        formValue.produtos.forEach((p) => {
          const product = this.products().find((product) => product.id == Number(p.produtoId)!);
          if (product) {
            totalValue = product.valor * Number(p.quantidade)! + totalValue;
          }
        });
      }
      this.controls.valorTotal.setValue(totalValue.toString(), { emitEvent: false });
    });
  }

  loadOrders() {
    this.loading.set(true);

    this.service
      .load()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.orders.set(response.content);
      });
  }

  loadProducts() {
    this.loading.set(true);

    this.productService
      .load()
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe((response) => {
        this.products.set(response.content);
      });
  }

  hideConfirmDialog() {
    this.id.set(undefined);
    this.confirmationDialog = false;
  }

  hideSaveDialog() {
    this.saveDialog = false;
  }

  showConfirmDialog(id: number) {
    this.id.set(id);
    this.confirmationDialog = true;
  }

  showSaveDialog(id?: number) {
    if (id) {
      const order = this.orders().find((o) => o.id == id);
      if (order) {
        const dto = {
          nomeComprador: order.nomeComprador,
          nomeForncedor: order.nomeForncedor,
          valorTotal: order.valorTotal.toString(),
          //   produtos: order.produtos.map((p) => p.id) as any,
        };
        this.formGroup.patchValue(dto);
      }
      this.id.set(id);
    }

    this.saveDialog = true;
  }

  handleAddProduct() {
    const product = this.nonNullableFormBuilder.group({
      produtoId: ['', Validators.required],
      quantidade: ['', Validators.required],
    });
    this.productsFormArray.push(product);
  }

  handleDeleteProduct(index: number) {
    this.productsFormArray.removeAt(index);
  }

  handleSave() {
    this.loading.set(true);
    this.service
      .save(this.formGroup?.value as any, this.id())
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe(() => {
        this.messageService.add({ severity: 'success', summary: `Pedido ${this.id() ? 'editado' : 'criado'} com sucesso!` });
        this.loadOrders();
        this.hideSaveDialog();
      });
  }

  handleDelete() {
    this.loading.set(false);
    this.service
      .delete(this.id()!)
      .pipe(finalize(() => this.loading.set(false)))
      .subscribe(() => {
        this.messageService.add({ severity: 'success', summary: 'Pedido deletado com sucesso!' });
        this.loadOrders();
        this.hideConfirmDialog();
      });
  }

  get controls() {
    return this.formGroup.controls;
  }

  get productsFormArray() {
    return this.formGroup.controls.produtos;
  }
}
