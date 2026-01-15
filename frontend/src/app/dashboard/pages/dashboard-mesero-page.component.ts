import { Component } from '@angular/core';

/**
 * Dashboard operativo para el rol de mesero.
 */
@Component({
  selector: 'app-dashboard-mesero-page',
  standalone: true,
  template: `
    <div class="flex flex-col gap-6">
      <div class="flex flex-wrap items-center justify-between gap-4">
        <div>
          <h2 class="text-2xl font-semibold text-slate-900">Dashboard de Mesero</h2>
          <p class="text-sm text-slate-500">Turno actual con métricas rápidas.</p>
        </div>
        <span class="rounded-full bg-emerald-100 px-3 py-1 text-xs font-semibold text-emerald-700">Turno activo</span>
      </div>

      <div class="grid gap-4 md:grid-cols-3">
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Mesas asignadas</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">12</p>
          <p class="text-sm text-slate-500">5 con pedidos nuevos</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Ordenes en curso</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">8</p>
          <p class="text-sm text-slate-500">3 listas para entregar</p>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-4 shadow-sm">
          <p class="text-xs uppercase text-slate-400">Propinas estimadas</p>
          <p class="mt-2 text-3xl font-semibold text-slate-900">$ 46.80</p>
          <p class="text-sm text-slate-500">Ultimas 6 horas</p>
        </div>
      </div>

      <div class="grid gap-4 lg:grid-cols-2">
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold text-slate-900">Mesas prioritarias</h3>
            <span class="text-xs text-slate-500">Dummy data</span>
          </div>
          <ul class="mt-4 space-y-3">
            <li class="flex items-center justify-between rounded-xl bg-slate-50 p-3">
              <div>
                <p class="text-sm font-semibold text-slate-900">Mesa 7</p>
                <p class="text-xs text-slate-500">3 platos · Espera 12 min</p>
              </div>
              <span class="rounded-full bg-amber-100 px-2 py-1 text-xs font-semibold text-amber-700">Pendiente</span>
            </li>
            <li class="flex items-center justify-between rounded-xl bg-slate-50 p-3">
              <div>
                <p class="text-sm font-semibold text-slate-900">Mesa 15</p>
                <p class="text-xs text-slate-500">2 bebidas · Espera 6 min</p>
              </div>
              <span class="rounded-full bg-sky-100 px-2 py-1 text-xs font-semibold text-sky-700">En cocina</span>
            </li>
          </ul>
        </div>
        <div class="rounded-2xl border border-slate-200 bg-white p-5 shadow-sm">
          <div class="flex items-center justify-between">
            <h3 class="text-base font-semibold text-slate-900">Checklist de turno</h3>
            <span class="text-xs text-slate-500">Actualizado</span>
          </div>
          <div class="mt-4 space-y-3 text-sm text-slate-600">
            <div class="flex items-center gap-2 rounded-xl bg-emerald-50 px-3 py-2 text-emerald-700">
              <span class="text-base">✔</span>
              <span>Briefing completado</span>
            </div>
            <div class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2">
              <span class="text-base">•</span>
              <span>Actualizar estado de 5 mesas</span>
            </div>
            <div class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2">
              <span class="text-base">•</span>
              <span>Registrar cierre parcial</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  `
})
export class DashboardMeseroPageComponent {}
