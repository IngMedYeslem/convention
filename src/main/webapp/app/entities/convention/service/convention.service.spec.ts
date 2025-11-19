import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IConvention } from '../convention.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../convention.test-samples';

import { ConventionService, RestConvention } from './convention.service';

const requireRestSample: RestConvention = {
  ...sampleWithRequiredData,
  dateSignConv: sampleWithRequiredData.dateSignConv?.format(DATE_FORMAT),
  dateDebutConv: sampleWithRequiredData.dateDebutConv?.format(DATE_FORMAT),
  echeanceConv: sampleWithRequiredData.echeanceConv?.format(DATE_FORMAT),
  dateCreation: sampleWithRequiredData.dateCreation?.toJSON(),
  dateModification: sampleWithRequiredData.dateModification?.toJSON(),
};

describe('Convention Service', () => {
  let service: ConventionService;
  let httpMock: HttpTestingController;
  let expectedResult: IConvention | IConvention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(ConventionService);
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

    it('should create a Convention', () => {
      const convention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(convention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Convention', () => {
      const convention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(convention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Convention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Convention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Convention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addConventionToCollectionIfMissing', () => {
      it('should add a Convention to an empty array', () => {
        const convention: IConvention = sampleWithRequiredData;
        expectedResult = service.addConventionToCollectionIfMissing([], convention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(convention);
      });

      it('should not add a Convention to an array that contains it', () => {
        const convention: IConvention = sampleWithRequiredData;
        const conventionCollection: IConvention[] = [
          {
            ...convention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addConventionToCollectionIfMissing(conventionCollection, convention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Convention to an array that doesn't contain it", () => {
        const convention: IConvention = sampleWithRequiredData;
        const conventionCollection: IConvention[] = [sampleWithPartialData];
        expectedResult = service.addConventionToCollectionIfMissing(conventionCollection, convention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(convention);
      });

      it('should add only unique Convention to an array', () => {
        const conventionArray: IConvention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const conventionCollection: IConvention[] = [sampleWithRequiredData];
        expectedResult = service.addConventionToCollectionIfMissing(conventionCollection, ...conventionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const convention: IConvention = sampleWithRequiredData;
        const convention2: IConvention = sampleWithPartialData;
        expectedResult = service.addConventionToCollectionIfMissing([], convention, convention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(convention);
        expect(expectedResult).toContain(convention2);
      });

      it('should accept null and undefined values', () => {
        const convention: IConvention = sampleWithRequiredData;
        expectedResult = service.addConventionToCollectionIfMissing([], null, convention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(convention);
      });

      it('should return initial array if no Convention is added', () => {
        const conventionCollection: IConvention[] = [sampleWithRequiredData];
        expectedResult = service.addConventionToCollectionIfMissing(conventionCollection, undefined, null);
        expect(expectedResult).toEqual(conventionCollection);
      });
    });

    describe('compareConvention', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareConvention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 2390 };
        const entity2 = null;

        const compareResult1 = service.compareConvention(entity1, entity2);
        const compareResult2 = service.compareConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 2390 };
        const entity2 = { id: 7509 };

        const compareResult1 = service.compareConvention(entity1, entity2);
        const compareResult2 = service.compareConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 2390 };
        const entity2 = { id: 2390 };

        const compareResult1 = service.compareConvention(entity1, entity2);
        const compareResult2 = service.compareConvention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
