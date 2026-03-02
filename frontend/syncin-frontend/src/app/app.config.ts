import {ApplicationConfig, importProvidersFrom, provideBrowserGlobalErrorListeners} from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import {provideHttpClient, withInterceptors} from '@angular/common/http';
import {jwtInterceptor} from './interceptor/jwt-interceptor';
import {House, KeyRound, LogIn, LogOut, LucideAngularModule, Rss, User, UserCog, UserPlus} from 'lucide-angular';

export const appConfig: ApplicationConfig = {
  providers: [
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes),
    provideHttpClient(
      withInterceptors([jwtInterceptor])
    ),
    importProvidersFrom(
      LucideAngularModule.pick({ Rss, User, UserCog, KeyRound, LogOut, LogIn, House, UserPlus })
    )
  ]
};
