package monsterservice.ControllerTest;

import monsterservice.controller.MonsterController;
import monsterservice.handleExceptionError.HandleExceptionError;
import monsterservice.model.Monster;
import monsterservice.service.MonsterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
public class MonsterControllerTest {
    private Monster mockMonster() {
        Monster mockMonster = new Monster();
        mockMonster.setId(1);
        mockMonster.setName("name");
        mockMonster.setHealth(400);
        return mockMonster;

    }

    @InjectMocks
    private MonsterController monsterController;

    @Mock
    private MonsterService monsterService;

    @Test
    void getGreetingTest() {
        String response = monsterController
                .getGreeting();
        assertEquals("Hi there", response);
//        return null;
    }


//    @Test
//    void postCreateTest() {
//        doReturn(mockMonster()).when(monsterService)
//                .postCreateMonsterService(any(Monster.class));
//
//        Monster response = monsterController.postCreate(new Monster());
//
//        assertEquals(mockMonster().getId(), response.getId());
//        assertEquals(mockMonster().getId(), response.getName());
//        assertEquals(mockMonster().getId(), response.getHealth());
//    }

    @Test
    void postCreateTest() {
        doReturn(mockMonster()).when(monsterService)
                .postCreateMonsterService(any(Monster.class));

        Monster response = monsterController.postCreate(new Monster());

        assertEquals(mockMonster().getId(), response.getId());
        assertEquals(mockMonster().getName(), response.getName());
        assertEquals(mockMonster().getHealth(), response.getHealth());

    }

    @Test
    void getMonsterTest() {
        List<Monster> monsterList = new ArrayList<>();
        monsterList.add(mockMonster());

        doReturn(monsterList)
                .when(monsterService).getAllMonsterService();

        List<Monster> response = monsterController.getAll();
        assertEquals(monsterList, response);
    }

    @Test
    void getInformationTest() {
        Optional<Monster> monsterOptional = Optional.of(mockMonster());

        doReturn(monsterOptional)
                .when(monsterService).getInformationService(any(Integer.class));

        Optional<Monster> response = monsterController.getInformation(1);

        assertTrue(response.isPresent());

//        assertFalse(response.isPresent());

    }

    @Test
    void putUpdateTestSuccess() throws HandleExceptionError {
        doReturn(mockMonster())
                .when(monsterService)
                .updateMonsterByIdService(any(Monster.class));
        ResponseEntity<Monster> response
                = monsterController.putUpdate(new Monster());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockMonster().getId()
                , Objects.requireNonNull(response.getBody().getId()));
    }

    @Test
    void putUpdateTestFail() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Error"))
                .when(monsterService)
                .updateMonsterByIdService(any(Monster.class));
        ResponseEntity<Monster> response
                = monsterController.putUpdate(new Monster());
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody().getId());
    }

    @Test
    void deleteTestSuccess() throws HandleExceptionError {
        doReturn(true)
                .when(monsterService)
                .deleteMonsterService(any(Integer.class));
        ResponseEntity<Boolean> response
                = monsterController.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(Boolean.TRUE, response.getBody());
//        assertEquals(true, response.getBody());
//        assertTrue(response.getBody());

    }


    @Test
    void deleteTestFail() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Error"))
                .when(monsterService)
                .deleteMonsterService(any(Integer.class));
        ResponseEntity<Boolean> response
                = monsterController.delete(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//        assertEquals(Boolean.FALSE, response.getBody());
//        assertEquals(false, response.getBody());
        assertFalse(response.getBody());
    }


    @Test
    void attackTestSuccess() throws HandleExceptionError {
        doReturn("updatesuccess")
                .when(monsterService)
                .attackMonsterService(any(Integer.class), any(Integer.class));
        ResponseEntity<String> response
                = monsterController.putAttack(1, 100);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updatesuccess", response.getBody());
    }

    @Test
    void putAttackTestFailCanNotUpdate() throws HandleExceptionError {
        doThrow(new HandleExceptionError("Can't update"))
                .when(monsterService)
                .attackMonsterService(any(Integer.class),
                        any(Integer.class));

        ResponseEntity<String> response
                = monsterController.putAttack(1, 100);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Can't update", response.getBody());
    }

    @Test
    void attackTestFailDataNotFound() throws HandleExceptionError {

        doThrow(new HandleExceptionError("Data Not Found"))
                .when(monsterService)
                .attackMonsterService(any(Integer.class), any(Integer.class));
        ResponseEntity<String> response
                = monsterController.putAttack(1, 100);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data Not Found", response.getBody());

    }

}
