import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { take } from 'rxjs';
import { environment } from '../../environments/environment';
import { PaginatedData } from '../types/common.types';
import { Product } from '../types/products.types';

@Injectable({ providedIn: 'root' })
export class ProductsService {
  httpClient = inject(HttpClient);
  resourceUrl = `${environment.apiUrl}/produto`;

  load(page = 0, size = 20) {
    return this.httpClient.get<PaginatedData<Product>>(this.resourceUrl, {
      params: {
        page,
        size,
      },
    });
  }

  save(payload: Object, id?: number) {
    return id ? this.update(payload, id) : this.create(payload);
  }

  delete(id: number) {
    return this.httpClient.delete(`${this.resourceUrl}/${id}`).pipe(take(1));
  }

  private create(payload: Object) {
    return this.httpClient.post<Product>(this.resourceUrl, payload).pipe(take(1));
  }

  private update(payload: Object, id: number) {
    return this.httpClient.put<Product>(`${this.resourceUrl}/${id}`, payload).pipe(take(1));
  }
}
