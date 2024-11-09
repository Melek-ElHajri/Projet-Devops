import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Etudiant } from 'src/models/Etudiant';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})

export class Services {

  private baseUrl1 = `http://localhost:8089/tpfoyer/etudiant`;

  constructor(private http: HttpClient) {}

  getEtudiants(): Observable<Etudiant[]> {
    return this.http.get<Etudiant[]>(`${this.baseUrl1}/find`);
  }
  retrieveEtudiant(equipeid: number): Observable<Etudiant> {
    return this.http.get<Etudiant>(`${this.baseUrl1}/findById/${equipeid}`);
  }
  addEtudiant(e: Etudiant): Observable<any> {
    return this.http.post<any>(`${this.baseUrl1}/add-etudiant`,e);
  }
  removeEtudiant(equipeid: number): Observable<Etudiant> {
    return this.http.delete<Etudiant>(`${this.baseUrl1}/deleteById/${equipeid}`);
  }
  updateEtudiant(e: Etudiant): Observable<any> {
    return this.http.put<any>(`${this.baseUrl1}/addOrUpdate/`, e);
  }

}
