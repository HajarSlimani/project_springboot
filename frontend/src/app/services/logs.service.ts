import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Log } from '../models/models';

@Injectable({
  providedIn: 'root'
})
export class LogsService {
  private apiUrl = 'http://localhost:8080/logs';

  constructor(private http: HttpClient) { }

  getByPlantId(plantId: number): Observable<Log[]> {
    return this.http.get<Log[]>(`${this.apiUrl}/plant/${plantId}`);
  }

  create(log: Log): Observable<Log> {
    return this.http.post<Log>(this.apiUrl, log);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
