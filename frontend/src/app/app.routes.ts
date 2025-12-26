import { Routes } from '@angular/router';
import { LoginComponent } from './auth/login/login.component';
import { RegisterComponent } from './auth/register/register.component';
import { PlantsListComponent } from './plants/plants-list/plants-list.component';
import { PlantDetailComponent } from './plants/plant-detail/plant-detail.component';
import { PlantFormComponent } from './plants/plant-form/plant-form.component';
import { authGuard } from './guards/auth.guard';

export const routes: Routes = [
  { path: '', redirectTo: '/plants', pathMatch: 'full' },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'plants', component: PlantsListComponent, canActivate: [authGuard] },
  { path: 'plants/new', component: PlantFormComponent, canActivate: [authGuard] },
  { path: 'plants/:id', component: PlantDetailComponent, canActivate: [authGuard] },
  { path: 'plants/:id/edit', component: PlantFormComponent, canActivate: [authGuard] },
  { path: '**', redirectTo: '/plants' }
];
