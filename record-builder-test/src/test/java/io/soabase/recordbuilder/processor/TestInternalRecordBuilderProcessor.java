/**
 * Copyright 2019 Jordan Zimmerman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.soabase.recordbuilder.processor;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.RecordComponentElement;
import javax.lang.model.type.DeclaredType;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class TestInternalRecordBuilderProcessor {
    @Test
    void testIsRequiredFieldExplicitNotNull() {
        var recordElement = mock(RecordComponentElement.class);
        var annotation1 = mockAnnotation("SomeAnnotation");
        var annotation2 = mockAnnotation("NotNull");
        List<AnnotationMirror> annotations = List.of(annotation1, annotation2);

        when(recordElement.getAnnotationMirrors()).thenAnswer(invocationOnMock -> annotations);

        Assertions.assertTrue(InternalRecordBuilderProcessor.isRequiredField(recordElement));
    }

    @Test
    void testIsRequiredFieldExplicitNonNull() {
        var recordElement = mock(RecordComponentElement.class);


        var annotation1 = mockAnnotation("SomeAnnotation");
        var annotation2 = mockAnnotation("NonNull");
        List<AnnotationMirror> annotations = List.of(annotation1, annotation2);

        when(recordElement.getAnnotationMirrors()).thenAnswer(invocationOnMock -> annotations);

        Assertions.assertTrue(InternalRecordBuilderProcessor.isRequiredField(recordElement));
    }

    @Test
    void testIsRequiredFieldTestNotNullable() {
        var recordElement = mock(RecordComponentElement.class);
        var annotation1 = mockAnnotation("SomeAnnotation");
        var annotation2 = mockAnnotation("SomeOtherAnnotation");
        List<AnnotationMirror> annotations = List.of(annotation1, annotation2);

        when(recordElement.getAnnotationMirrors()).thenAnswer(invocationOnMock -> annotations);

        Assertions.assertTrue(InternalRecordBuilderProcessor.isRequiredField(recordElement));
    }

    @Test
    void testIsRequiredFieldTestNullable() {
        var recordElement = mock(RecordComponentElement.class);
        var annotation1 = mockAnnotation("SomeAnnotation");
        var annotation2 = mockAnnotation("Nullable");
        List<AnnotationMirror> annotations = List.of(annotation1, annotation2);

        when(recordElement.getAnnotationMirrors()).thenAnswer(invocationOnMock -> annotations);

        Assertions.assertFalse(InternalRecordBuilderProcessor.isRequiredField(recordElement));
    }

    @Test
    void testNullableAndNotNulllable() {
        var recordElement = mock(RecordComponentElement.class);
        var name = mock(Name.class);

        var annotation1 = mockAnnotation("NotNull");
        var annotation2 = mockAnnotation("Nullable");
        List<AnnotationMirror> annotations = List.of(annotation1, annotation2);

        when(name.toString()).thenReturn("foo");
        when(recordElement.getSimpleName()).thenReturn(name);
        when(recordElement.getAnnotationMirrors()).thenAnswer(invocationOnMock -> annotations);

        Throwable ex =Assertions.assertThrows(IllegalStateException.class,
                () -> InternalRecordBuilderProcessor.isRequiredField(recordElement));

        Assertions.assertEquals("Element foo has both a nullable and not-nullable annotation", ex.getMessage());
    }

    static AnnotationMirror mockAnnotation(String annotationName) {
        var mockAnnotation = mock(AnnotationMirror.class);
        var mockAnnotationType = mock(DeclaredType.class);
        var mockElement = mock(Element.class);
        var mockName = mock(Name.class);

        when(mockName.toString()).thenReturn(annotationName);
        when(mockElement.getSimpleName()).thenReturn(mockName);
        when(mockAnnotationType.asElement()).thenReturn(mockElement);
        when(mockAnnotation.getAnnotationType()).thenReturn(mockAnnotationType);

        return mockAnnotation;
    }
}
