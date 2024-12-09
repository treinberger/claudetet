import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  template: `
    <div class="app-container">
      <header class="nx-margin-bottom-m">
        <h1 class="nx-margin-bottom-0">Raffle System</h1>
      </header>

      <main>
        <router-outlet></router-outlet>
      </main>
    </div>
  `,
  styles: [`
    .app-container {
      padding: 2rem;
      max-width: 1200px;
      margin: 0 auto;
    }

    header {
      border-bottom: 1px solid #eee;
      padding-bottom: 1rem;
    }
  `]
})
export class AppComponent {
  title = 'Raffle System';
}