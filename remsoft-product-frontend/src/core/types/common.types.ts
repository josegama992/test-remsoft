export type PaginatedData<T> = {
  content: T[];
  size: number;
  totalElements: number;
  totalPages: number;
  pageNumber: number;
};
