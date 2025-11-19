import { TestBed } from '@angular/core/testing';
import { HttpTestingController, provideHttpClientTesting } from '@angular/common/http/testing';
import { provideHttpClient } from '@angular/common/http';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IDetailConvention } from '../detail-convention.model';
import { sampleWithFullData, sampleWithNewData, sampleWithPartialData, sampleWithRequiredData } from '../detail-convention.test-samples';

import { DetailConventionService, RestDetailConvention } from './detail-convention.service';

const requireRestSample: RestDetailConvention = {
  ...sampleWithRequiredData,
  dateCreation: sampleWithRequiredData.dateCreation?.format(DATE_FORMAT),
};

describe('DetailConvention Service', () => {
  let service: DetailConventionService;
  let httpMock: HttpTestingController;
  let expectedResult: IDetailConvention | IDetailConvention[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideHttpClientTesting()],
    });
    expectedResult = null;
    service = TestBed.inject(DetailConventionService);
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

    it('should create a DetailConvention', () => {
      const detailConvention = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(detailConvention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a DetailConvention', () => {
      const detailConvention = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(detailConvention).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a DetailConvention', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of DetailConvention', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a DetailConvention', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addDetailConventionToCollectionIfMissing', () => {
      it('should add a DetailConvention to an empty array', () => {
        const detailConvention: IDetailConvention = sampleWithRequiredData;
        expectedResult = service.addDetailConventionToCollectionIfMissing([], detailConvention);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailConvention);
      });

      it('should not add a DetailConvention to an array that contains it', () => {
        const detailConvention: IDetailConvention = sampleWithRequiredData;
        const detailConventionCollection: IDetailConvention[] = [
          {
            ...detailConvention,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addDetailConventionToCollectionIfMissing(detailConventionCollection, detailConvention);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a DetailConvention to an array that doesn't contain it", () => {
        const detailConvention: IDetailConvention = sampleWithRequiredData;
        const detailConventionCollection: IDetailConvention[] = [sampleWithPartialData];
        expectedResult = service.addDetailConventionToCollectionIfMissing(detailConventionCollection, detailConvention);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailConvention);
      });

      it('should add only unique DetailConvention to an array', () => {
        const detailConventionArray: IDetailConvention[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const detailConventionCollection: IDetailConvention[] = [sampleWithRequiredData];
        expectedResult = service.addDetailConventionToCollectionIfMissing(detailConventionCollection, ...detailConventionArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const detailConvention: IDetailConvention = sampleWithRequiredData;
        const detailConvention2: IDetailConvention = sampleWithPartialData;
        expectedResult = service.addDetailConventionToCollectionIfMissing([], detailConvention, detailConvention2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(detailConvention);
        expect(expectedResult).toContain(detailConvention2);
      });

      it('should accept null and undefined values', () => {
        const detailConvention: IDetailConvention = sampleWithRequiredData;
        expectedResult = service.addDetailConventionToCollectionIfMissing([], null, detailConvention, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(detailConvention);
      });

      it('should return initial array if no DetailConvention is added', () => {
        const detailConventionCollection: IDetailConvention[] = [sampleWithRequiredData];
        expectedResult = service.addDetailConventionToCollectionIfMissing(detailConventionCollection, undefined, null);
        expect(expectedResult).toEqual(detailConventionCollection);
      });
    });

    describe('compareDetailConvention', () => {
      it('should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareDetailConvention(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('should return false if one entity is null', () => {
        const entity1 = { id: 29255 };
        const entity2 = null;

        const compareResult1 = service.compareDetailConvention(entity1, entity2);
        const compareResult2 = service.compareDetailConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey differs', () => {
        const entity1 = { id: 29255 };
        const entity2 = { id: 24555 };

        const compareResult1 = service.compareDetailConvention(entity1, entity2);
        const compareResult2 = service.compareDetailConvention(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('should return false if primaryKey matches', () => {
        const entity1 = { id: 29255 };
        const entity2 = { id: 29255 };

        const compareResult1 = service.compareDetailConvention(entity1, entity2);
        const compareResult2 = service.compareDetailConvention(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
