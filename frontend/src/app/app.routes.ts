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
import { MesasListPageComponent } from './mesas/pages/mesas-list-page.component';
import { MesasFormPageComponent } from './mesas/pages/mesas-form-page.component';
import { MenuListPageComponent } from './menu/pages/menu-list-page.component';
import { MenuFormPageComponent } from './menu/pages/menu-form-page.component';
import { OrdersPageComponent } from './orders/pages/orders-page.component';
import { OrdersListPageComponent } from './orders/pages/orders-list-page.component';
import { OrdersShellPageComponent } from './orders/pages/orders-shell-page.component';
import { OrdersConfigPageComponent } from './orders/pages/orders-config-page.component';

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
    canActivate: [AuthGuard],
    data: { title: 'Mesas' },
    children: [
      { path: '', component: MesasListPageComponent },
      { path: 'new', component: MesasFormPageComponent },
      { path: ':id/edit', component: MesasFormPageComponent },
      { path: 'configuracion', redirectTo: '', pathMatch: 'full' }
    ]
  },
  {
    path: 'menu',
    canActivate: [AuthGuard, RoleGuard],
    data: { roles: ['SUPERADMINISTRADOR', 'ADMINISTRADOR'] },
    children: [
      { path: '', component: MenuListPageComponent },
      { path: 'new', component: MenuFormPageComponent },
      { path: ':id/edit', component: MenuFormPageComponent }
    ]
  },
  {
    path: 'ordenes',
    canActivate: [AuthGuard],
    data: { title: 'Ordenes' },
    children: [
      { path: '', redirectTo: 'lista', pathMatch: 'full' },
      { path: 'lista', component: OrdersListPageComponent },
      { path: 'toma', component: OrdersPageComponent },
      { path: 'configuracion', component: OrdersConfigPageComponent },
      { path: ':id/edit', component: OrdersPageComponent }
    ],
    component: OrdersShellPageComponent
  },
  {
    path: 'despachador',
    component: PlaceholderPageComponent,
    canActivate: [AuthGuard],
    data: { title: 'Despachador' }
  }
];
