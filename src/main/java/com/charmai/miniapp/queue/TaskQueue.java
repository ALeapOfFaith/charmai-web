package com.charmai.miniapp.queue;

import java.util.concurrent.CopyOnWriteArrayList;

public class TaskQueue {

    private final CopyOnWriteArrayList<String> queue;

    public TaskQueue() {
        queue = new CopyOnWriteArrayList<>();
    }

    public int enqueue(String userId) {
        queue.add(userId);
        return queue.indexOf(userId);
    }

    public String dequeue() {
        if (!queue.isEmpty()) {
            return queue.remove(0);
        }
        return null;
    }

    public Integer getPosition(String userId) {
        return !queue.contains(userId) ? 0 : (queue.indexOf(userId) + 1);
    }
    public Integer total() {
        return queue.size();
    }

    public Integer getTime(String userId) {
        return 30 * (!queue.contains(userId) ? 0 : (queue.indexOf(userId) + 1));
    }

    public void toTop(String userId) {
        queue.add(0, userId);
    }

    public CopyOnWriteArrayList<String> getQueue() {
        return queue;
    }
}

