import { Routes } from '@angular/router';
import { AuthGuard } from './core/guards/auth.guard';
import { RoleGuard } from './core/guards/role.guard';
import { LoginPageComponent } from './auth/pages/login-page.component';
import { DashboardPageComponent } from './dashboard/pages/dashboard-page.component';
import { RoleModulesPageComponent } from './role-modules/pages/role-modules-page.component';
import { UsersListPageComponent } from './users/pages/users-list-page.component';
import { UserDetailPageComponent } from './users/pages/user-detail-page.component';
import { UserFormPageComponent } from './users/pages/user-form-page.component';
import { ProfilePageComponent } from './users/pages/profile-page.component';
import { PlaceholderPageComponent } from './placeholders/pages/placeholder-page.component';

/**
 * Definición de rutas principales de la aplicación.
 */
export const routes: Routes = [
  { path: '', redirectTo: 'dashboard', pathMatch: 'full' },
  { path: 'login', component: LoginPageComponent },
  {
    path: 'dashboard',
    component: DashboardPageComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SUPERADMINISTRADOR', 'ADMINISTRADOR', 'MESERO', 'DESPACHADOR'] }
  },
  {
    path: 'users',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SUPERADMINISTRADOR', 'ADMINISTRADOR'] },
    children: [
      { path: '', component: UsersListPageComponent },
      { path: 'new', component: UserFormPageComponent },
      { path: ':id', component: UserDetailPageComponent },
      { path: ':id/edit', component: UserFormPageComponent }
    ]
  },
  {
    path: 'role-modules',
    component: RoleModulesPageComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SUPERADMINISTRADOR', 'ADMINISTRADOR'] }
  },
  { path: 'profile', component: ProfilePageComponent, canActivate: [AuthGuard] },
  {
    path: 'mesas',
    component: PlaceholderPageComponent,
    canActivate: [AuthGuard],
    data: { title: 'Mesas' }
  },
  {
    path: 'menu',
    component: PlaceholderPageComponent,
    canActivate: [AuthGuard],
    data: { title: 'Menu' }
  },
  {
    path: 'ordenes',
    component: PlaceholderPageComponent,
    canActivate: [AuthGuard],
    data: { title: 'Ordenes' }
  },
  {
    path: 'despachador',
    component: PlaceholderPageComponent,
    canActivate: [AuthGuard],
    data: { title: 'Despachador' }
  }
];
