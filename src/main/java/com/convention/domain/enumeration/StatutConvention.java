package com.convention.domain.enumeration;

/**
 * The StatutConvention enumeration.
 */
public enum StatutConvention {
    BROUILLON, // مسودة — تعديل حر للمصلحة
    SOUMIS, // مرسل — في انتظار موافقة القطاع
    APPROUVE_DEPT, // موافق عليه من القطاع — في انتظار موافقة الإدارة
    ACTIVE, // موافق عليه نهائياً ومفعّل
    SUSPENDUE, // موقوف مؤقتاً
    TERMINEE, // منتهي
    ANNULEE, // ملغى
    REJETE, // مرفوض — يعود للمصلحة للمراجعة
}
