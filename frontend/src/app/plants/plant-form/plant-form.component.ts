import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { PlantsService } from '../../services/plants.service';
import { Plant } from '../../models/models';

@Component({
  selector: 'app-plant-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './plant-form.component.html',
  styleUrl: './plant-form.component.scss'
})
export class PlantFormComponent implements OnInit {
  plant: Plant = { name: '', species: '', location: '', image: '' };
  isEditMode = false;
  loading = false;
  error = '';
  imagePreview: string | null = null;

  constructor(
    private plantsService: PlantsService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.isEditMode = true;
      this.loadPlant(+id);
    }
  }

  loadPlant(id: number) {
    this.loading = true;
    this.plantsService.getById(id).subscribe({
      next: (plant) => {
        this.plant = plant;
        this.imagePreview = plant.image || null;
        this.loading = false;
      },
      error: () => {
        this.error = 'Erreur chargement plante';
        this.loading = false;
      }
    });
  }

  onFileSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      // Vérifier la taille du fichier (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        this.error = 'L\'image est trop volumineuse (max 5MB)';
        return;
      }

      // Vérifier le type de fichier
      if (!file.type.startsWith('image/')) {
        this.error = 'Veuillez sélectionner une image';
        return;
      }

      this.error = '';
      
      // Créer une image pour la redimensionner
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const img = new Image();
        img.onload = () => {
          // Redimensionner l'image à une largeur max de 800px
          const canvas = document.createElement('canvas');
          const ctx = canvas.getContext('2d');
          
          let width = img.width;
          let height = img.height;
          const maxWidth = 800;
          const maxHeight = 800;
          
          if (width > height) {
            if (width > maxWidth) {
              height *= maxWidth / width;
              width = maxWidth;
            }
          } else {
            if (height > maxHeight) {
              width *= maxHeight / height;
              height = maxHeight;
            }
          }
          
          canvas.width = width;
          canvas.height = height;
          ctx?.drawImage(img, 0, 0, width, height);
          
          // Convertir en base64 avec compression (qualité 0.7)
          const compressedBase64 = canvas.toDataURL('image/jpeg', 0.7);
          this.imagePreview = compressedBase64;
          this.plant.image = compressedBase64;
        };
        img.src = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeImage() {
    this.imagePreview = null;
    this.plant.image = '';
  }

  save() {
    if (!this.plant.name) {
      this.error = 'Le nom est requis';
      return;
    }

    this.loading = true;
    this.error = '';

    const operation = this.isEditMode
      ? this.plantsService.update(this.plant.id!, this.plant)
      : this.plantsService.create(this.plant);

    operation.subscribe({
      next: () => {
        this.router.navigate(['/plants']);
      },
      error: (err) => {
        console.error('Erreur lors de l\'enregistrement:', err);
        if (err.status === 413) {
          this.error = 'L\'image est trop volumineuse pour le serveur';
        } else if (err.status === 400) {
          this.error = err.error?.message || 'Données invalides';
        } else {
          this.error = 'Erreur lors de l\'enregistrement. Veuillez réessayer.';
        }
        this.loading = false;
      }
    });
  }
}
