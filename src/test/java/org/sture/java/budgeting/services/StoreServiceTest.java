package org.sture.java.budgeting.services;

import org.sture.java.budgeting.BaseTest;
import org.sture.java.budgeting.store.dto.DTOConverter;
import org.sture.java.budgeting.store.StoreService;
import org.sture.java.budgeting.utils.DTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StoreServiceTest extends BaseTest {
    private StoreService<TestObject, TestObjectDTO> storeService;

    @BeforeEach
    void setUp() {
        storeService = new StoreService<>(
                "testObject.store",
                new TestObjectDTOConverter()) {
            @Override
            protected void Store(TestObjectDTO[] dtoObjects) {
                super.Store(dtoObjects);
            }
        };
        storeService.DeleteStoreIfExists();
    }

    @AfterEach
    void tearDown() {
        storeService.DeleteStoreIfExists();
        storeService = null;
    }

    @Test
    void TestStoringOneObject(){
        var object = new TestObject("somevar");
        storeService.Store(object);

        var readBack = storeService.Read();
        assertNotNull(readBack);

        assertEquals(1, readBack.length);

        var readObject = readBack[0];
        assertEquals(readObject.var, object.var);
    }

    @Test
    void TestStoringArray() {
        var dataArray = new TestObject[] {
                new TestObject("a"),
                new TestObject("b"),
                new TestObject("c"),
                new TestObject("d")
        };

        storeService.Store(dataArray);
        var readBack = storeService.Read();
        assertArrayEquals(dataArray, readBack);
    }

    @Test
    void TestStoringList(){
        ArrayList<TestObject> dataList = new ArrayList<>();
        dataList.add(new TestObject("a"));
        dataList.add(new TestObject("b"));
        dataList.add(new TestObject("c"));
        dataList.add(new TestObject("d"));

        storeService.Store(dataList);
        var readBack = storeService.Read();
        assertNotNull(readBack);
        assertEquals(dataList.size(), readBack.length);
        for(int i = 0; i < readBack.length; i++)
            assertEquals(dataList.get(i), readBack[i]);
    }

    public static class TestObject {

        public String var;
        public TestObject(String var){
            this.var = var;
        }

        @Override
        public boolean equals(Object o){
            if(o instanceof TestObject t){
                return var.equals(t.var);
            }
            return false;
        }
    }

    public static class TestObjectDTO implements DTO<TestObject>, Serializable {
        public String var;
        public TestObjectDTO(String var){
            this.var = var;
        }

        @Override
        public TestObject Convert() {
            return new TestObject(var);
        }
    }

    public static class TestObjectDTOConverter implements DTOConverter<TestObject, TestObjectDTO> {

        @Override
        public TestObjectDTO ConvertToDTO(TestObject testObject) {
            return new TestObjectDTO(testObject.var);
        }

        @Override
        public TestObjectDTO[] DoMagicToDTO(List<TestObjectDTO> lst) {
            return lst.toArray(new TestObjectDTO[0]);
        }

        @Override
        public TestObject ConvertFromDTO(TestObjectDTO t) {
            return t.Convert();
        }

        @Override
        public TestObject[] DoMagicFromDTO(List<TestObject> lst) {
            return lst.toArray(new TestObject[0]);
        }
    }
}