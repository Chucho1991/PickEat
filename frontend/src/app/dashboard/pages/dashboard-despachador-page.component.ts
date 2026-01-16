import { Component } from '@angular/core';

/**
 * Dashboard operativo para el rol de despachador.
 */
@Component({
  selector: 'app-dashboard-despachador-page',
  standalone: true,
  template: `
    <div class="flex flex-col gap-6">
      <div class="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 class="text-2xl font-semibold text-slate-900">Dashboard de Despachador</h2>
          <p class="text-sm text-slate-500">Control de órdenes listas y rutas de salida.</p>
        </div>
        <span class="rounded-full bg-indigo-100 px-3 py-1 text-xs font-semibold text-indigo-700">Centro de salida</span>
      </div>

      <div class="grid gap-4 md:grid-cols-2 xl:grid-cols-4">
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Ordenes listas</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">14</p>
          <p class="text-sm text-slate-500">4 esperando retiro</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Despachos activos</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">6</p>
          <p class="text-sm text-slate-500">2 en ruta externa</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Tiempo promedio</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">18 min</p>
          <p class="text-sm text-slate-500">Objetivo 20 min</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Incidencias</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">2</p>
          <p class="text-sm text-slate-500">Requieren revisión</p>
        </div>
      </div>

      <div class="grid gap-4 lg:grid-cols-2">
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold text-slate-900">Rutas prioritarias</h3>
            <span class="text-xs text-slate-500">Dummy data</span>
          </div>
          <div class="mt-4 space-y-3">
            <div class="flex items-center justify-between rounded-xl bg-slate-50 p-3">
              <div>
                <p class="text-sm font-semibold text-slate-900">Ruta A - Mesa 3</p>
                <p class="text-xs text-slate-500">2 ordenes · Salida 5 min</p>
              </div>
              <span class="rounded-full bg-emerald-100 px-2 py-1 text-xs font-semibold text-emerald-700">Listo</span>
            </div>
            <div class="flex items-center justify-between rounded-xl bg-slate-50 p-3">
              <div>
                <p class="text-sm font-semibold text-slate-900">Ruta C - Mesa 18</p>
                <p class="text-xs text-slate-500">1 orden · Salida 9 min</p>
              </div>
              <span class="rounded-full bg-amber-100 px-2 py-1 text-xs font-semibold text-amber-700">Preparando</span>
            </div>
          </div>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold text-slate-900">Estado de cocina</h3>
            <span class="text-xs text-slate-500">Actualizado 2 min</span>
          </div>
          <div class="mt-4 grid gap-3">
            <div class="rounded-xl border border-slate-200 p-3">
              <p class="text-sm font-semibold text-slate-900">Linea caliente</p>
              <p class="text-xs text-slate-500">5 pedidos en proceso</p>
            </div>
            <div class="rounded-xl border border-slate-200 p-3">
              <p class="text-sm font-semibold text-slate-900">Linea fria</p>
              <p class="text-xs text-slate-500">2 pedidos en espera</p>
            </div>
            <div class="rounded-xl border border-slate-200 p-3">
              <p class="text-sm font-semibold text-slate-900">Bebidas</p>
              <p class="text-xs text-slate-500">3 pedidos pendientes</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardDespachadorPageComponent {}
