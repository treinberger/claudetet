import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { UserListComponent } from './components/user-list/user-list.component';
import { RaffleListComponent } from './components/raffle-list/raffle-list.component';
import { RaffleDetailComponent } from './components/raffle-detail/raffle-detail.component';
import { CreateRaffleComponent } from './components/create-raffle/create-raffle.component';

const routes: Routes = [
  { path: 'users', component: UserListComponent },
  { path: 'raffles/create', component: CreateRaffleComponent },  // Add this before raffles/:id
  { path: 'raffles', component: RaffleListComponent },
  { path: 'raffles/:id', component: RaffleDetailComponent },
  { path: '', redirectTo: '/users', pathMatch: 'full' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }