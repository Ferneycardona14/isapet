import { provideHttpClient } from '@angular/common/http';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute, convertToParamMap, provideRouter, Router } from '@angular/router';
import { of } from 'rxjs';
import { EditarAfiliadoComponent } from './editar-afiliado.component';

describe('EditarAfiliadoComponent', () => {
  let fixture: ComponentFixture<EditarAfiliadoComponent>;
  let http: HttpTestingController;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [EditarAfiliadoComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        { provide: ActivatedRoute, useValue: { paramMap: of(convertToParamMap({ id: '7' })) } }
      ]
    }).compileComponents();
    http = TestBed.inject(HttpTestingController);
    fixture = TestBed.createComponent(EditarAfiliadoComponent);
  });

  afterEach(() => http.verify());

  it('carga afiliado y mantiene la identificación de solo lectura', () => {
    fixture.detectChanges();
    http.expectOne('/api/v1/afiliados/7').flush(affiliate());
    fixture.detectChanges();

    const readonlyInput = fixture.nativeElement.querySelector('input[readonly]') as HTMLInputElement;
    expect(readonlyInput.value).toBe('ID-7');
    expect(fixture.componentInstance.form.controls.nombres.value).toBe('Ana');
  });

  it('envía actualización de datos sin incluir identificación', () => {
    spyOn(TestBed.inject(Router), 'navigate').and.resolveTo(true);
    fixture.detectChanges();
    http.expectOne('/api/v1/afiliados/7').flush(affiliate());
    fixture.detectChanges();
    fixture.componentInstance.form.patchValue({ nombres: 'Ana Maria' });
    fixture.componentInstance.guardar();

    const request = http.expectOne('/api/v1/afiliados/7');
    expect(request.request.method).toBe('PUT');
    expect(request.request.body).toEqual({ nombres: 'Ana Maria', apellidos: 'Lopez', celular: '3001234567', correo: 'ana@example.com', direccion: 'Calle 1' });
    expect(request.request.body.numeroIdentificacion).toBeUndefined();
    request.flush({ ...affiliate(), nombres: 'Ana Maria' });
  });

  it('muestra mensaje amigable para afiliado inexistente', () => {
    fixture.detectChanges();
    http.expectOne('/api/v1/afiliados/7').flush(
      { status: 404, message: 'No existe', errors: [] },
      { status: 404, statusText: 'Not Found' }
    );
    fixture.detectChanges();
    expect(fixture.nativeElement.textContent).toContain('No se encontró el afiliado solicitado.');
  });

  it('mantiene las mismas reglas Unicode, numéricas y de correo al editar', () => {
    fixture.detectChanges();
    http.expectOne('/api/v1/afiliados/7').flush(affiliate());
    fixture.detectChanges();
    fixture.componentInstance.form.patchValue({ nombres: '6', apellidos: 'Pérez Gómez', correo: 'usuario@gmail.com' });
    expect(fixture.componentInstance.form.valid).toBeTrue();

    fixture.componentInstance.form.controls.nombres.setValue('Nombre2');
    fixture.componentInstance.form.controls.celular.setValue('+573001234567');
    fixture.componentInstance.form.controls.correo.setValue('usuario@gmail');
    expect(fixture.componentInstance.form.controls.nombres.invalid).toBeTrue();
    expect(fixture.componentInstance.form.controls.celular.invalid).toBeTrue();
    expect(fixture.componentInstance.form.controls.correo.invalid).toBeTrue();
  });

  it('rechaza campos obligatorios compuestos solo por espacios y muestra el mensaje', () => {
    fixture.detectChanges();
    http.expectOne('/api/v1/afiliados/7').flush(affiliate());
    fixture.detectChanges();
    fixture.componentInstance.form.patchValue({ nombres: '   ', apellidos: '   ', direccion: '   ' });
    fixture.componentInstance.form.controls.nombres.markAsTouched();
    fixture.detectChanges();

    expect(fixture.componentInstance.form.invalid).toBeTrue();
    expect(fixture.nativeElement.textContent).toContain('Este campo es obligatorio.');
  });

  function affiliate() {
    return { id: 7, numeroIdentificacion: 'ID-7', nombres: 'Ana', apellidos: 'Lopez', celular: '3001234567', correo: 'ana@example.com', direccion: 'Calle 1' };
  }
});
