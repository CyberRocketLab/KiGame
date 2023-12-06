package client.converter;

import client.move.EMoves;
import messagesbase.messagesfromclient.EMove;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerConverterTest {

   @Test
    public void RightMove_convertToServerMove_shouldWork() {
        ServerConverter serverConverter = new ServerConverter();

        assertEquals(EMove.Right, serverConverter.convertToEMove(EMoves.Right));
    }

    @Test
    public void LeftMove_convertToServerMove_shouldWork() {
        ServerConverter serverConverter = new ServerConverter();

        assertEquals(EMove.Left, serverConverter.convertToEMove(EMoves.Left));
    }

    @Test
    public void UPMove_convertToServerMove_shouldWork() {
        ServerConverter serverConverter = new ServerConverter();

        assertEquals(EMove.Up, serverConverter.convertToEMove(EMoves.Up));
    }

    @Test
    public void DownMove_convertToServerMove_shouldWork() {
        ServerConverter serverConverter = new ServerConverter();

        assertEquals(EMove.Down, serverConverter.convertToEMove(EMoves.Down));
    }



}