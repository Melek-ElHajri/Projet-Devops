import { Component } from '@angular/core';
import { Etudiant } from 'src/models/Etudiant';
import { Services } from 'src/services/EtudiantService';

@Component({
  selector: 'app-etudiant',
  templateUrl: './etudiant.component.html',
  styleUrls: ['./etudiant.component.css']
})
export class EtudiantComponent {
  etudiants: Etudiant[] = [];
  addVisible: boolean = false;
  currentStep: number = 1;  // To track the current step in the multi-step form
  etudiant = {
    nomEt: '',
    prenomEt: '',
    cin: 0,
    ecole: '',
  };
  modify: boolean = false;
  modifyid: number = 0;
  newEtudiant: Etudiant = {};

  constructor(private etudiantService: Services) {}

  ngOnInit() {
    this.getEtudiants();
  }

  getEtudiants() {
    this.etudiantService.getEtudiants().subscribe(
      (response) => {
        if (response) {
          this.etudiants = response;
          console.log("Retrieved etudiants: ", this.etudiants);
        } else {
          console.log("No data returned from server");
        }
      },
      (error) => {
        console.error('Error retrieving Etudiants:', error);
      }
    );
  }

  // Popup Position
  popupFreightId: number | undefined;
  popupPosition = { top: 0, left: 0 };
  togglePopup(event: MouseEvent, etudiantId: number | undefined) {
    event.stopPropagation();
    this.popupFreightId = etudiantId;
    this.popupPosition = {
      top: event.clientY - 40,
      left: event.clientX - 275
    };
  }

  cancelPopup() {
    this.popupFreightId = 0;
  }

  // Toggle Add Popup
  toggleAddPopup() {
    this.addVisible = !this.addVisible;
    this.currentStep = 1;  // Reset to the first step
    if (!this.addVisible) this.resetForm();
  }

  // Reset Form
  resetForm() {
    this.etudiant = {
      nomEt: '',
      prenomEt: '',
      cin: 0,
      ecole: ''
    };
    this.newEtudiant = {};
    this.modify = false;
    this.currentStep = 1;  // Ensure form starts from the first step
  }

  // Modify Etudiant
  modifyF(etudiant: Etudiant) {
    this.modify = true;
    this.modifyid = etudiant.idEtudiant!;
    this.newEtudiant = { ...etudiant };
    this.etudiant = {
      nomEt: etudiant.nomEt || '',
      prenomEt: etudiant.prenomEt || '',
      cin: etudiant.cin || 0,
      ecole: etudiant.ecole || ''
    };
    this.toggleAddPopup();
    this.cancelPopup();
  }

  // Add or Update Etudiant
  addEtudiant() {
    this.newEtudiant = { ...this.etudiant };
    if (!this.modify) {
      this.etudiantService.addEtudiant(this.newEtudiant).subscribe({
        next: () => {
          this.getEtudiants();
          this.toggleAddPopup();
          alert('Étudiant ajouté avec succès');
        },
        error: () => {
          alert('Erreur lors de l\'ajout de l\'étudiant');
        }
      });
    } else {
      this.etudiantService.updateEtudiant(this.newEtudiant).subscribe({
        next: () => {
          this.modify = false;
          this.getEtudiants();
          this.toggleAddPopup();
          alert('Étudiant mis à jour avec succès');
        },
        error: () => {
          alert('Erreur lors de la mise à jour de l\'étudiant');
        }
      });
    }
  }

  // Remove Etudiant
  removeEtudiant(id: number | undefined) {
    this.etudiantService.removeEtudiant(id!).subscribe({
      next: () => {
        this.getEtudiants();
      },
      error: () => {
        alert('Erreur lors de la suppression de l\'étudiant');
      }
    });
  }

  // Navigation for Multi-Step Form
  nextStep() {
    if (this.currentStep < 3) {
      this.currentStep++;
    }
  }

  previousStep() {
    if (this.currentStep > 1) {
      this.currentStep--;
    }
  }
}
