import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { IDetailFacture } from '../detail-facture.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../detail-facture.test-samples';

import { DetailFactureService } from './detail-facture.service';

const requireRestSample: IDetailFacture = {
  ...sampleWithRequiredData,
};

describe('DetailFacture Service', () => {
  let service: DetailFactureService;
  let httpMock: HttpTestingController;
  let expectedResult: IDetailFacture | IDetailFacture[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DetailFactureService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a DetailFacture', () => {
      const detailFacture = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(detailFacture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetailFacture', () => {
      const detailFacture = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(detailFacture).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetailFacture', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetailFacture', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DetailFacture', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDetailFactureToCollectionIfMissing', () => {
      it('should add a DetailFacture to an empty array', () => {
        const detailFacture: IDetailFacture = sampleWithRequiredData;
        expectedResult = service.addDetailFactureToCollectionIfMissing([], detailFacture);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailFacture);
      });

      it('should not add a DetailFacture to an array that contains it', () => {
        const detailFacture: IDetailFacture = sampleWithRequiredData;
        const detailFactureCollection: IDetailFacture[] = [
          {
            ...detailFacture,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDetailFactureToCollectionIfMissing(detailFactureCollection, detailFacture);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetailFacture to an array that doesn't contain it", () => {
        const detailFacture: IDetailFacture = sampleWithRequiredData;
        const detailFactureCollection: IDetailFacture[] = [sampleWithPartialData];
        expectedResult = service.addDetailFactureToCollectionIfMissing(detailFactureCollection, detailFacture);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailFacture);
      });

      it('should add only unique DetailFacture to an array', () => {
        const detailFactureArray: IDetailFacture[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const detailFactureCollection: IDetailFacture[] = [sampleWithRequiredData];
        expectedResult = service.addDetailFactureToCollectionIfMissing(detailFactureCollection, ...detailFactureArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detailFacture: IDetailFacture = sampleWithRequiredData;
        const detailFacture2: IDetailFacture = sampleWithPartialData;
        expectedResult = service.addDetailFactureToCollectionIfMissing([], detailFacture, detailFacture2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailFacture);
        expect(expectedResult).toContain(detailFacture2);
      });

      it('should accept null and undefined values', () => {
        const detailFacture: IDetailFacture = sampleWithRequiredData;
        expectedResult = service.addDetailFactureToCollectionIfMissing([], null, detailFacture, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailFacture);
      });

      it('should return initial array if no DetailFacture is added', () => {
        const detailFactureCollection: IDetailFacture[] = [sampleWithRequiredData];
        expectedResult = service.addDetailFactureToCollectionIfMissing(detailFactureCollection, undefined, null);
        expect(expectedResult).toEqual(detailFactureCollection);
      });
    });

    describe('compareDetailFacture', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDetailFacture(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 17963 };
        const entity2 = null;

        const compareResult1 = service.compareDetailFacture(entity1, entity2);
        const compareResult2 = service.compareDetailFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 17963 };
        const entity2 = { id: 23380 };

        const compareResult1 = service.compareDetailFacture(entity1, entity2);
        const compareResult2 = service.compareDetailFacture(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 17963 };
        const entity2 = { id: 17963 };

        const compareResult1 = service.compareDetailFacture(entity1, entity2);
        const compareResult2 = service.compareDetailFacture(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
