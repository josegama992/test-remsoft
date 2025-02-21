import { Routes } from '@angular/router';
import { AppLayout } from './app/layout/components/app.layout';
import { OrdersComponent } from './app/pages/orders/orders.component';
import { ProductsComponent } from './app/pages/products/products.component';

export const appRoutes: Routes = [
  {
    path: '',
    component: AppLayout,
    children: [
      { path: '', component: OrdersComponent },
      { path: 'products', component: ProductsComponent },
    ],
  },
];
