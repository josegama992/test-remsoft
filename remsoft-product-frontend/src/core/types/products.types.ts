import { FormControl, FormGroup } from '@angular/forms';

export interface Product {
  id: number;
  nome: string;
  valor: number;
}

export type ProductForm = FormGroup<{
  nome: FormControl<string>;
  valor: FormControl<string>;
}>;
