import { FormArray, FormControl, FormGroup } from '@angular/forms';
import { Product } from './products.types';

export interface Order {
  id: number;
  nomeComprador: string;
  nomeForncedor: string;
  valorTotal: number;
  produtos: Product[];
}

export type OrderForm = FormGroup<{
  nomeComprador: FormControl<string>;
  nomeForncedor: FormControl<string>;
  valorTotal: FormControl<string>;
  produtos: FormArray<OrderProductForm>;
}>;

export type OrderProductForm = FormGroup<{
  produtoId: FormControl<string>;
  quantidade: FormControl<string>;
}>;
