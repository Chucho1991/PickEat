import { CommonModule } from '@angular/common';
import { Component, DestroyRef, OnInit, computed, inject, signal } from '@angular/core';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';
import { catchError, of, switchMap, timer } from 'rxjs';
import { DispatcherApiService, DispatcherItemDto } from '../../core/services/dispatcher-api.service';

/**
 * Dashboard operativo para el rol de despachador.
 */
@Component({
  selector: 'app-dashboard-despachador-page',
  standalone: true,
  imports: [CommonModule],
  template: `
    <div class="flex flex-col gap-6">
      <div class="flex flex-wrap items-start justify-between gap-4">
        <div>
          <h2 class="text-2xl font-semibold text-slate-900">Dashboard de Despachador</h2>
          <p class="text-sm text-slate-500">
            Lista dinámica de comandas registradas en MongoDB con actualización continua.
          </p>
        </div>
        <div class="flex flex-wrap items-center gap-3 rounded-2xl border border-slate-200 bg-white px-4 py-3 shadow-sm">
          <div class="text-xs text-slate-500">
            Retraso visible
            <span class="font-semibold text-slate-700">{{ delaySeconds() }}s</span>
          </div>
          <input
            class="w-24 rounded-lg border border-slate-200 px-2 py-1 text-sm text-slate-700 focus:border-indigo-400 focus:outline-none"
            type="number"
            min="0"
            [value]="delaySeconds()"
            (input)="updateDelaySeconds($any($event.target).value)"
            aria-label="Configurar retraso de visibilidad"
          />
        </div>
      </div>

      <div class="grid gap-4 md:grid-cols-3">
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Comandas visibles</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">{{ visibleItems().length }}</p>
          <p class="text-sm text-slate-500">Listas para despachar</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">En espera</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">{{ pendingItemsCount() }}</p>
          <p class="text-sm text-slate-500">Aparecen tras {{ delaySeconds() }}s</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Última actualización</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">
            {{ lastUpdated() ? (lastUpdated() | date: 'shortTime') : '--' }}
          </p>
          <p class="text-sm text-slate-500">Refresco cada {{ refreshIntervalSeconds }}s</p>
        </div>
      </div>

      <div *ngIf="errorMessage()" class="rounded-2xl border border-rose-200 bg-rose-50 p-4 text-sm text-rose-700">
        {{ errorMessage() }}
      </div>

      <div class="grid gap-4 lg:grid-cols-2">
        <div
          *ngFor="let item of visibleItems(); trackBy: trackByItem"
          class="flex flex-col gap-4 rounded-2xl border border-slate-200 bg-white p-5 shadow-sm"
          [ngClass]="getColorToken(item).border"
        >
          <div class="flex items-start justify-between gap-3">
            <div>
              <div class="flex flex-wrap items-center gap-2 text-xs font-semibold">
                <span class="rounded-full px-2 py-1" [ngClass]="getColorToken(item).badge">Mesa {{ item.mesa }}</span>
                <span class="text-slate-500">Orden {{ item.orderId }}</span>
              </div>
              <h3 class="mt-2 text-lg font-semibold text-slate-900">{{ item.plato }}</h3>
              <p class="text-sm text-slate-500">Cantidad: {{ item.cantidad }}</p>
            </div>
            <span class="text-xs text-slate-400" *ngIf="item.addedAt">
              {{ item.addedAt | date: 'shortTime' }}
            </span>
          </div>

          <div class="grid grid-cols-2 gap-3">
            <button
              class="flex items-center justify-center gap-2 rounded-xl bg-emerald-600 px-3 py-3 text-sm font-semibold text-white shadow-sm transition hover:bg-emerald-700"
              type="button"
              title="Despachado"
              aria-label="Despachado"
              (click)="markAsDispatched(item)"
            >
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true" class="h-5 w-5">
                <path d="M5 12L10 17L19 8" stroke="currentColor" stroke-width="1.8" stroke-linecap="round" stroke-linejoin="round"></path>
              </svg>
              Despachado
            </button>
            <button
              class="flex items-center justify-center gap-2 rounded-xl bg-rose-600 px-3 py-3 text-sm font-semibold text-white shadow-sm transition hover:bg-rose-700"
              type="button"
              title="Cancelado"
              aria-label="Cancelado"
              (click)="markAsCancelled(item)"
            >
              <svg viewBox="0 0 24 24" fill="none" aria-hidden="true" class="h-5 w-5">
                <path d="M7 7L17 17" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"></path>
                <path d="M17 7L7 17" stroke="currentColor" stroke-width="1.8" stroke-linecap="round"></path>
              </svg>
              Cancelado
            </button>
          </div>
        </div>
      </div>

      <div *ngIf="visibleItems().length === 0" class="rounded-2xl border border-dashed border-slate-200 bg-white p-6 text-center">
        <p class="text-sm text-slate-500">No hay comandas visibles en este momento.</p>
      </div>
    </div>
  `
})
export class DashboardDespachadorPageComponent implements OnInit {
  private dispatcherApi = inject(DispatcherApiService);
  private destroyRef = inject(DestroyRef);

  items = signal<DispatcherItemDto[]>([]);
  delaySeconds = signal(20);
  lastUpdated = signal<Date | null>(null);
  errorMessage = signal<string | null>(null);
  refreshIntervalSeconds = 2;

  visibleItems = computed(() => {
    const delayMs = this.delaySeconds() * 1000;
    const now = Date.now();
    return this.items()
      .filter(item => {
        if (!item.addedAt) {
          return true;
        }
        return new Date(item.addedAt).getTime() <= now - delayMs;
      })
      .sort((a, b) => {
        const dateA = a.addedAt ? new Date(a.addedAt).getTime() : 0;
        const dateB = b.addedAt ? new Date(b.addedAt).getTime() : 0;
        return dateA - dateB;
      });
  });

  pendingItemsCount = computed(() => this.items().length - this.visibleItems().length);

  private colorTokens = [
    { border: 'border-l-4 border-emerald-500', badge: 'bg-emerald-100 text-emerald-700' },
    { border: 'border-l-4 border-sky-500', badge: 'bg-sky-100 text-sky-700' },
    { border: 'border-l-4 border-indigo-500', badge: 'bg-indigo-100 text-indigo-700' },
    { border: 'border-l-4 border-amber-500', badge: 'bg-amber-100 text-amber-700' },
    { border: 'border-l-4 border-rose-500', badge: 'bg-rose-100 text-rose-700' },
    { border: 'border-l-4 border-teal-500', badge: 'bg-teal-100 text-teal-700' },
    { border: 'border-l-4 border-purple-500', badge: 'bg-purple-100 text-purple-700' },
    { border: 'border-l-4 border-cyan-500', badge: 'bg-cyan-100 text-cyan-700' }
  ];

  /**
   * Inicia el refresco continuo de la lista de comandas.
   */
  ngOnInit() {
    timer(0, this.refreshIntervalSeconds * 1000)
      .pipe(
        switchMap(() =>
          this.dispatcherApi.listDispatcherItems().pipe(
            catchError(() => {
              this.errorMessage.set('No fue posible cargar las comandas del despachador.');
              return of([] as DispatcherItemDto[]);
            })
          )
        ),
        takeUntilDestroyed(this.destroyRef)
      )
      .subscribe(items => {
        this.items.set(items);
        this.lastUpdated.set(new Date());
        if (this.errorMessage()) {
          this.errorMessage.set(null);
        }
      });
  }

  /**
   * Ajusta el retraso de visibilidad para nuevas comandas.
   */
  updateDelaySeconds(value: string) {
    const parsed = Number(value);
    if (Number.isFinite(parsed) && parsed >= 0) {
      this.delaySeconds.set(parsed);
    }
  }

  /**
   * Determina el color asignado a una combinación mesa-orden.
   */
  getColorToken(item: DispatcherItemDto) {
    const hash = this.hashString(`${item.orderId}-${item.mesa}`);
    return this.colorTokens[hash % this.colorTokens.length];
  }

  /**
   * Marca una comanda como despachada.
   */
  markAsDispatched(_item: DispatcherItemDto) {}

  /**
   * Marca una comanda como cancelada.
   */
  markAsCancelled(_item: DispatcherItemDto) {}

  /**
   * Identificador estable para filas del tablero.
   */
  trackByItem(_index: number, item: DispatcherItemDto) {
    return `${item.orderId}-${item.mesa}-${item.plato}`;
  }

  private hashString(value: string) {
    let hash = 0;
    for (let i = 0; i < value.length; i += 1) {
      hash = (hash << 5) - hash + value.charCodeAt(i);
      hash |= 0;
    }
    return Math.abs(hash);
  }
}
