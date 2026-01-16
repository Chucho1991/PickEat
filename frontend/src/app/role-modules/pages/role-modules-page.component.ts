import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ModuleAccess, ModuleAccessService } from '../../core/services/module-access.service';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

/**
 * Página para configurar módulos visibles por rol.
 */
@Component({
  selector: 'app-role-modules-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="flex flex-col gap-6">
      <div>
        <h2 class="text-2xl font-semibold text-slate-900">Accesos por rol</h2>
        <p class="text-sm text-slate-500">
          Selecciona qué módulos estarán visibles para cada rol del sistema.
        </p>
      </div>

      <div class="flex flex-wrap items-center gap-4 rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
        <label class="text-sm font-medium text-slate-700">Rol</label>
        <select
          class="min-w-[220px] rounded-xl border border-slate-200 bg-white px-3 py-2 text-sm text-slate-700 shadow-sm focus:border-indigo-500 focus:outline-none"
          [(ngModel)]="selectedRole"
          (ngModelChange)="loadModules()"
        >
          <option *ngFor="let role of roles" [value]="role">{{ role }}</option>
        </select>
        <span class="text-xs text-slate-500" *ngIf="statusMessage()">{{ statusMessage() }}</span>
      </div>

      <div class="grid gap-4 md:grid-cols-2">
        <div
          class="flex items-center justify-between rounded-2xl border border-slate-200 bg-white p-4 shadow-sm"
          *ngFor="let module of modules()"
        >
          <div>
            <p class="text-sm font-semibold text-slate-900">{{ module.label }}</p>
            <p class="text-xs text-slate-500">{{ module.key }}</p>
          </div>
          <label class="inline-flex items-center gap-2 text-sm text-slate-600">
            <input
              type="checkbox"
              class="h-4 w-4 rounded border-slate-300 text-indigo-600 focus:ring-indigo-500"
              [checked]="module.enabled"
              (change)="toggleModule(module.key, $event.target.checked)"
            />
            <span>{{ module.enabled ? 'Visible' : 'Oculto' }}</span>
          </label>
        </div>
      </div>

      <div class="flex flex-wrap items-center gap-3">
        <button
          class="rounded-xl bg-indigo-600 px-4 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-700"
          (click)="saveModules()"
        >
          Guardar cambios
        </button>
        <span class="text-xs text-slate-500" *ngIf="saveMessage()">{{ saveMessage() }}</span>
      </div>
    </div>
  `
})
export class RoleModulesPageComponent implements OnInit {
  private moduleAccessService = inject(ModuleAccessService);
  private destroyRef = inject(DestroyRef);
  roles = ['SUPERADMINISTRADOR', 'ADMINISTRADOR', 'MESERO', 'DESPACHADOR'];
  selectedRole = 'SUPERADMINISTRADOR';
  modules = signal<ModuleAccess[]>([]);
  statusMessage = signal('');
  saveMessage = signal('');
  hasModules = computed(() => this.modules().length > 0);

  /**
   * Inicializa la carga de módulos para el rol seleccionado.
   */
  ngOnInit() {
    this.loadModules();
  }

  /**
   * Carga la configuración de módulos para el rol actual.
   */
  loadModules() {
    this.statusMessage.set('Cargando módulos...');
    this.saveMessage.set('');
    this.moduleAccessService
      .getModulesForRole(this.selectedRole)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (modules) => {
          this.modules.set(modules);
          this.statusMessage.set(modules.length === 0 ? 'Sin módulos configurados.' : '');
        },
        error: () => {
          this.modules.set([]);
          this.statusMessage.set('No se pudo cargar la configuración.');
        }
      });
  }

  /**
   * Alterna la visibilidad de un módulo en memoria.
   *
   * @param key llave del módulo.
   * @param enabled estado seleccionado.
   */
  toggleModule(key: string, enabled: boolean) {
    this.modules.set(
      this.modules().map((module) => (module.key === key ? { ...module, enabled } : module))
    );
  }

  /**
   * Envía la configuración actual al backend.
   */
  saveModules() {
    if (!this.hasModules()) {
      this.saveMessage.set('No hay módulos para actualizar.');
      return;
    }
    this.saveMessage.set('Guardando...');
    this.moduleAccessService
      .updateRoleModules(this.selectedRole, this.modules())
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: () => {
          this.saveMessage.set('Configuración guardada.');
        },
        error: () => {
          this.saveMessage.set('No se pudo guardar la configuración.');
        }
      });
  }
}
