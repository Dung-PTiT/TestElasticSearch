/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import org.zkoss.zk.ui.event.*;
import org.zkoss.zk.ui.util.*;
import org.zkoss.zul.*;
import org.zkoss.lang.*;
import org.zkoss.zk.ui.Component;

public class TestComposer extends GenericForwardComposer {

    // Constants
    private static final String QUEUE = "queue";
    private static final String START = "start";
    private static final int ONE_HUNDRED = 100;

    // ZK components
    private Progressmeter progressmeter;
    private Timer timer;

    // Progress counter [0,100]
    private int progress;

    @Override
    public void doAfterCompose(Component comp) throws Exception {
        super.doAfterCompose(comp);
        if (!EventQueues.exists(QUEUE)) {
            
            EventQueue queue = EventQueues.lookup(QUEUE);
            //setUp queue
            progress = 0;
            queue.subscribe(new EventListener() { // Asynchronous (worker)
                public void onEvent(Event event) {
                    doLongOperation();
                }
            }, new EventListener() {
                public void onEvent(Event event) { // Synchronous (callback)
                    EventQueues.remove(QUEUE);
                }
            });

            //Start queue
            queue.publish(new Event(START)); // Triggers the processing. The event name does not matter.
            timer.start();
        }
    }

    private void doLongOperation() {
        for (int i = 1; i <= 10; i++) {
            progress += 10;
        }
    }

    public void onTimer$timer() {
        progressmeter.setValue(progress);
        if (progressmeter.getValue() == ONE_HUNDRED) {
            timer.stop();
        }
    }
}
