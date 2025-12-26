import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Plant } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class PlantsService {
  private apiUrl = 'http://localhost:8080/plants';

  constructor(private http: HttpClient) { }

  getAll(): Observable<Plant[]> {
    return this.http.get<Plant[]>(this.apiUrl);
  }

  getById(id: number): Observable<Plant> {
    return this.http.get<Plant>(`${this.apiUrl}/${id}`);
  }

  create(plant: Plant): Observable<Plant> {
    return this.http.post<Plant>(this.apiUrl, plant);
  }

  update(id: number, plant: Plant): Observable<Plant> {
    return this.http.put<Plant>(`${this.apiUrl}/${id}`, plant);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
