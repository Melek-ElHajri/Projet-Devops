import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { EtudiantComponent } from './etudiant/etudiant.component';

const routes: Routes = [
  { path:'' , redirectTo: 'etudiant', pathMatch: 'full'},
  { path: 'etudiant', component : EtudiantComponent,  },

  { path: '**', component :  EtudiantComponent}
  ];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
