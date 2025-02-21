import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { take } from 'rxjs';
import { PaginatedData } from '../types/common.types';
import { Order } from '../types/orders.types';
import { environment } from '../../environments/environment';

@Injectable({ providedIn: 'root' })
export class OrdersService {
  httpClient = inject(HttpClient);
  resourceUrl = `${environment.apiUrl}/pedido`  

  load(page = 0, size = 20) {
    return this.httpClient.get<PaginatedData<Order>>(this.resourceUrl, {
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
    return this.httpClient.post<Order>(this.resourceUrl, payload).pipe(take(1));
  }

  private update(payload: Object, id: number) {
    return this.httpClient.put<Order>(`${this.resourceUrl}/${id}`, payload).pipe(take(1));
  }
}
