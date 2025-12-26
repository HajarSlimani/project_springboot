import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, ActivatedRoute, RouterModule } from '@angular/router';
import { PlantsService } from '../../services/plants.service';
import { TasksService } from '../../services/tasks.service';
import { LogsService } from '../../services/logs.service';
import { Plant, Task, Log } from '../../models/models';

@Component({
  selector: 'app-plant-detail',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule],
  templateUrl: './plant-detail.component.html',
  styleUrl: './plant-detail.component.scss'
})
export class PlantDetailComponent implements OnInit {
  plant: Plant | null = null;
  tasks: Task[] = [];
  logs: Log[] = [];
  loading = true;
  
  showTaskForm = false;
  showLogForm = false;
  taskMarkingDone: Set<number> = new Set();
  
  newTask: Task = { type: '', frequencyDays: 7, lastDone: new Date().toISOString().split('T')[0], completed: false };
  newLog: Log = { date: new Date().toISOString().split('T')[0], note: '', image: '' };
  
  logImagePreview: string | null = null;

  constructor(
    private plantsService: PlantsService,
    private tasksService: TasksService,
    private logsService: LogsService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.loadPlant(+id);
      this.loadTasks(+id);
      this.loadLogs(+id);
    }
  }

  loadPlant(id: number) {
    this.plantsService.getById(id).subscribe({
      next: (plant) => {
        this.plant = plant;
        this.loading = false;
      },
      error: () => {
        this.router.navigate(['/plants']);
      }
    });
  }

  loadTasks(plantId: number) {
    this.tasksService.getByPlantId(plantId).subscribe({
      next: (tasks) => {
        // Ajouter la propriété completed si elle n'existe pas
        this.tasks = tasks.map(task => ({
          ...task,
          completed: task.completed ?? false
        }));
      },
      error: () => {}
    });
  }

  loadLogs(plantId: number) {
    this.logsService.getByPlantId(plantId).subscribe({
      next: (logs) => {
        this.logs = logs.sort((a, b) => 
          new Date(b.date).getTime() - new Date(a.date).getTime()
        );
      },
      error: () => {}
    });
  }

  toggleTaskForm() {
    this.showTaskForm = !this.showTaskForm;
    if (this.showTaskForm) {
      this.newTask = { type: '', frequencyDays: 7, lastDone: new Date().toISOString().split('T')[0], completed: false };
    }
  }

  saveTask() {
    if (!this.newTask.type || !this.plant?.id) return;

    this.newTask.plantId = this.plant.id;
    this.tasksService.create(this.newTask).subscribe({
      next: () => {
        this.loadTasks(this.plant!.id!);
        this.toggleTaskForm();
      },
      error: () => alert('Erreur ajout tâche')
    });
  }

  markTaskDone(task: Task) {
    if (!task.id) return;
    
    // Show animation
    this.taskMarkingDone.add(task.id);

    const updatedTask = { ...task, lastDone: new Date().toISOString().split('T')[0], completed: true };
    this.tasksService.update(task.id, updatedTask).subscribe({
      next: () => {
        this.loadTasks(this.plant!.id!);
        // Keep animation for 1.5 seconds then remove
        setTimeout(() => this.taskMarkingDone.delete(task.id!), 1500);
      },
      error: () => {
        this.taskMarkingDone.delete(task.id!);
        alert('Erreur mise à jour');
      }
    });
  }

  deleteTask(id: number | undefined) {
    if (!id || !confirm('Supprimer cette tâche ?')) return;

    this.tasksService.delete(id).subscribe({
      next: () => this.loadTasks(this.plant!.id!),
      error: () => alert('Erreur suppression')
    });
  }

  getNextDueDate(task: Task): Date | null {
    if (!task.lastDone) return null;
    const next = new Date(task.lastDone);
    next.setDate(next.getDate() + task.frequencyDays);
    return next;
  }

  isTaskOverdue(task: Task): boolean {
    const nextDue = this.getNextDueDate(task);
    return nextDue ? nextDue < new Date() : false;
  }

  toggleLogForm() {
    this.showLogForm = !this.showLogForm;
    if (this.showLogForm) {
      this.newLog = { date: new Date().toISOString().split('T')[0], note: '', image: '' };
      this.logImagePreview = null;
    }
  }

  onLogImageSelected(event: Event) {
    const file = (event.target as HTMLInputElement).files?.[0];
    if (file) {
      // Vérifier la taille du fichier (max 5MB)
      if (file.size > 5 * 1024 * 1024) {
        alert('L\'image est trop volumineuse (max 5MB)');
        return;
      }

      // Vérifier le type de fichier
      if (!file.type.startsWith('image/')) {
        alert('Veuillez sélectionner une image');
        return;
      }

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
          this.logImagePreview = compressedBase64;
          this.newLog.image = compressedBase64;
        };
        img.src = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  removeLogImage() {
    this.logImagePreview = null;
    this.newLog.image = '';
  }

  saveLog() {
    if (!this.newLog.date || !this.plant?.id) return;

    this.newLog.plantId = this.plant.id;
    this.logsService.create(this.newLog).subscribe({
      next: () => {
        this.loadLogs(this.plant!.id!);
        this.toggleLogForm();
      },
      error: () => alert('Erreur ajout note')
    });
  }

  deleteLog(id: number | undefined) {
    if (!id || !confirm('Supprimer cette note ?')) return;

    this.logsService.delete(id).subscribe({
      next: () => this.loadLogs(this.plant!.id!),
      error: () => alert('Erreur suppression')
    });
  }

  deletePlant() {
    if (!this.plant?.id || !confirm('Supprimer définitivement cette plante ?')) return;

    this.plantsService.delete(this.plant.id).subscribe({
      next: () => this.router.navigate(['/plants']),
      error: () => alert('Erreur suppression')
    });
  }
}
