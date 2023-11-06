package edu.yu.da;

import java.util.*;

import edu.yu.da.GeneticAlgorithmConfig.SelectionType;

public class ArithmeticPuzzle extends ArithmeticPuzzleBase{

    public class Solution implements SolutionI{
        private String augend;
        private String addend;
        private String sum;
        private List<Character> solution;
        private int nGenerations;

        public Solution(String augend, String addend, String sum, int nGenerations, List<Character> solution){
            this.augend = augend;
            this.addend = addend;
            this.sum = sum;
            this.nGenerations = nGenerations;
            this.solution = solution;
        }

        @Override
        public List<Character> solution() {
            return solution;
        }

        @Override
        public String getAugend() {
            return this.augend;
        }

        @Override
        public String getAddend() {
            return this.addend;
        }

        @Override
        public String getSum() {
            return this.sum;
        }

        @Override
        public int nGenerations() {
            return this.nGenerations;
        }
    }

    /**
     * Constructor.  Specifies the arithmetic puzzle to be solved in terms of an
     * augend, addend, and sum.
     * <p>
     * Representation: all characters are in the range A-Z, with each letter
     * represents a unique digit.  The puzzle to be solved specifies that the
     * augend and addend (each representing a number in base 10) sum to the
     * specified sum (also a number in base 10).  Each of these numbers is
     * represented with the most significant letter (digit) in position 0, next
     * most significant letter (digit) in position 1, and so on.  The numbers
     * need not be the same length: an "empty" digit is represented by the
     * "space" character.
     * <p>
     * Addition: Augend + Addend = Sum
     *
     * @param augend
     * @param addend
     * @param sum
     */
    private String augend;
    private String addend;
    private String sum;
    private Set<Character> letters;
    private Set<String> input;
    private int nGenerations = 1;
    private double totalFitness = 0;
    private final double FITNESS_SCALE = 10.0;

    public ArithmeticPuzzle(String augend, String addend, String sum) {
        super(augend, addend, sum);
        if(augend == null || addend == null || sum == null || augend.isBlank() || addend.isBlank() || sum.isBlank()){
            throw new IllegalArgumentException();
        }
        this.augend = augend;
        this.addend = addend;
        this.sum = sum;
        this.letters = new HashSet<>();
        this.input = new HashSet<>();
        this.input.add(augend);
        this.input.add(addend);
        this.input.add(sum);
        for(String s: input){
            for(int i = 0; i<s.length(); i++){
                if(s.charAt(i) != ' '){
                    this.letters.add(s.charAt((i)));
                }
            }
        }
    }

    @Override
    public SolutionI solveIt(GeneticAlgorithmConfig gac) {
        if(gac == null){
            throw new IllegalArgumentException();
        }
        List<List<Character>> population = new ArrayList<>();
        List<Character> original = new ArrayList<>(this.letters);
        if(original.size() < 10){
            int length = original.size();
            for(int i = length; i < 10; i++){
                original.add(' ');
            }
        }
        int fit = fitness(original);
        totalFitness += FITNESS_SCALE/(double)(fit + 1);
        if(fit == 0){
            //done
            return new Solution(this.augend, this.addend, this.sum, this.nGenerations, original);
        }
        population.add(original);
        for(int i = 1; i < gac.getInitialPopulationSize(); i++){
            List<Character> additional = new ArrayList<>(original);
            Collections.shuffle(additional);
            fit = fitness(additional);
            totalFitness += FITNESS_SCALE/(double)(fit + 1);
            if( fit == 0){
                return new Solution(this.augend, this.addend, this.sum, this.nGenerations, additional);
            }
            population.add(additional);
        }
        while(this.nGenerations < gac.getMaxGenerations()){
            this.nGenerations++;
            List<List<Character>> newPopulation = new ArrayList<>();
            //select
            while(newPopulation.size() <  population.size()){
                //Tournament selection
                if(gac.getSelectionType() == SelectionType.TOURNAMENT){
                    //choose two chromosomes at random
                    int random = (int)(Math.random() * population.size());
                    int random2 = (int)(Math.random() * population.size());
                    while(random == random2){
                        random2 = (int)(Math.random() * population.size());
                    }
                    if(fitness(population.get(random)) >= fitness(population.get(random2))){
                        newPopulation.add(population.get(random2));
                    }else{
                        newPopulation.add(population.get(random));
                    }
                }else{
                    //roulette selection
                    double rouletteValue = (Math.random() * totalFitness);
                    double cumulativeFitness = 0;
                    for (List<Character> characters : population) {
                        cumulativeFitness += (FITNESS_SCALE/(double)(fitness(characters) + 1));
                        if (cumulativeFitness > rouletteValue) {
                            newPopulation.add(characters);
                            break;
                        }
                    }
                }
            }
            for(int i = 0; i < newPopulation.size(); i++) {
                //crossover
                int random1 = (int) (Math.random() * newPopulation.size());
                int random2 = (int) (Math.random() * newPopulation.size());
                while (random1 == random2) {
                    random2 = (int) (Math.random() * newPopulation.size());
                }
                List<Character> list1 = newPopulation.get(random1);
                List<Character> list2 = newPopulation.get(random2);
                if (Math.random() < gac.getCrossoverProbability()) {
                    Set<Character> tempLettersChild1 = new HashSet<>(this.letters);
                    Set<Character> tempLettersChild2 = new HashSet<>(this.letters);
                    Random rand = new Random();
                    int crossoverPoint1 = rand.nextInt(list1.size() - 1) + 1;
                    int crossoverPoint2 = rand.nextInt(list1.size() - crossoverPoint1) + crossoverPoint1;
                    // Create empty child lists
                    List<Character> child1 = new ArrayList<>(list1.size());
                    List<Character> child2 = new ArrayList<>(list1.size());
                    // Copy the substrings between the crossover points from the first parent list to the first child list
                    for(int j = crossoverPoint1; j <= crossoverPoint2; j++) {
                        child1.add(list1.get(j));
                        tempLettersChild1.remove(list1.get(j));
                        child2.add(list2.get(j));
                        tempLettersChild2.remove(list2.get(j));
                    }
                    // Copy the remaining elements from the second parent list to the first child list, skipping any elements that are already in the child list
                    int index1 = crossoverPoint2 + 1;
                    int index2 = crossoverPoint2 + 1;
                    while (child1.size() < list1.size()) {
                        char element1 = list2.get(index1 % list2.size());
                        char element2 = list1.get(index2 % list1.size());
                        if (!child1.contains(element1)) {
                            child1.add(element1);
                            tempLettersChild1.remove(element1);
                        }else if (tempLettersChild1.size() != 0) {
                            char c = tempLettersChild1.iterator().next();
                            child1.add(c);
                            tempLettersChild1.remove(c);
                        }else{
                            child1.add(' ');
                        }
                        if (!child2.contains(element2)) {
                            child2.add(element2);
                            tempLettersChild2.remove(element2);
                        }else if(tempLettersChild2.size() != 0){
                            char c = tempLettersChild2.iterator().next();
                            child2.add(c);
                            tempLettersChild2.remove(c);
                        }else{
                            child2.add(' ');
                        }
                        index1++;
                        index2++;
                    }
                    population.set(random1, child1);
                    population.set(random2, child2);
                } else {
                    population.set(random1, list1);
                    population.set(random2, list2);
                }
                //mutate
                if(Math.random() < gac.getMutationProbability()){
                    int random = (int) (Math.random() * population.size());
                    Collections.shuffle(population.get(random));
                }
            }
            //check fitness of new population
            totalFitness = 0;
            for (List<Character> characters : population) {
                fit = fitness(characters);
                if (fit == 0) {
                    return new Solution(this.augend, this.addend, this.sum, this.nGenerations, characters);
                }
                totalFitness += FITNESS_SCALE/(double)(fit + 1);
            }
        }
        return new Solution(this.augend, this.addend, this.sum, this.nGenerations, Collections.emptyList());
    }

    public int fitness(List<Character> chromosome){
        String augendString = "";
        String addendString = "";
        String sumString = "";
        for(int i = 0; i < augend.length(); i++){
            if(augend.charAt(i) != ' '){
                augendString += chromosome.indexOf(augend.charAt(i));
            }

        }
        for(int i = 0; i < addend.length(); i++){
            if(addend.charAt(i) != ' '){
                addendString += chromosome.indexOf(addend.charAt(i));
            }

        }
        for(int i = 0; i < sum.length(); i++){
            if(sum.charAt(i) != ' '){
                sumString += chromosome.indexOf(sum.charAt(i));
            }
        }
        return Math.abs(Integer.parseInt(sumString) - (Integer.parseInt(augendString) + Integer.parseInt(addendString)));
    }

}
