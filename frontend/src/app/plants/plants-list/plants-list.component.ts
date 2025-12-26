import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterModule } from '@angular/router';
import { PlantsService } from '../../services/plants.service';
import { AuthService } from '../../services/auth.service';
import { Plant } from '../../models/models';

@Component({
  selector: 'app-plants-list',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './plants-list.component.html',
  styleUrl: './plants-list.component.scss'
})
export class PlantsListComponent implements OnInit {
  plants: Plant[] = [];
  loading = true;

  constructor(
    private plantsService: PlantsService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit() {
    this.loadPlants();
  }

  loadPlants() {
    this.loading = true;
    this.plantsService.getAll().subscribe({
      next: (plants) => {
        this.plants = plants;
        this.loading = false;
      },
      error: () => {
        this.loading = false;
      }
    });
  }

  logout() {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}
