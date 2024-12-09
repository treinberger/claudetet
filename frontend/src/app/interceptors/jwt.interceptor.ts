import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';

@Injectable()
export class JwtInterceptor implements HttpInterceptor {
    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const isApiUrl = request.url.startsWith(environment.apiUrl);
        
        if (isApiUrl) {
            // Add Basic Auth header
            const credentials = btoa('admin:admin123');
            request = request.clone({
                setHeaders: {
                    Authorization: `Basic ${credentials}`
                }
            });
        }

        return next.handle(request);
    }
}