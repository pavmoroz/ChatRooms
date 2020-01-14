package model;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class TmpStorage {
    public static final Set<UnusedName> userNames = new CopyOnWriteArraySet<>();
    public static final Set<UnusedName> chatRoomNames = new CopyOnWriteArraySet<>();
    public static final Set<UserDisconnection> listForDisconnections = new CopyOnWriteArraySet<>();
}
