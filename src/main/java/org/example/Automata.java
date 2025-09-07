package org.example;

import java.util.HashSet;
import java.util.Set;

public interface Automata {

    public Set<String> getStates();

    public Set<Character> getAlphabet();

    public String getStartState();

    public Set<String> getAcceptingStates();

}
