package com.app.jueee.concurrency.chapter06.common;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *  实现了交叉、选择和对种群或者个体的评估。 
 *	
 *	@author hzweiyongqiang
 */
public class GeneticOperators {
    
    /**
     *  创建一个种群
     *	@param numberOfIndividuals 种群的个体数
     *	@param size 染色体（本例中就是城市）的数目
     *	@return
     */
    public static Individual[] initialize(int numberOfIndividuals, int size) {
        Individual[] population = new Individual[numberOfIndividuals];
        for (int i = 0; i < population.length; i++) {
            population[i] = new Individual(size);
            initialize(population[i].getChromosomes());
        }
        return population;
    }
    
    /**
     *  以随机方式初始化某一个体的染色体，生成合法的个体（即每个城市只访问一次）
     *	@param chromosomes
     */
    public static void initialize(Integer[] chromosomes) {
        int size = chromosomes.length;
        List<Integer> values = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            values.add(i);
        }
        Collections.shuffle(values, new Random(System.nanoTime()));
        for (int i = 0; i < size; i++) {
            chromosomes[i] = values.get(i);
        }
    }
    
    /**
     *  实现了选择操作，获取一个种群的最优个体。
     *  它用一个数组返回这些个体。该数组的大小将是种群大小的一半。
     *  可以测试其他标准以确定选定个体的数目。使用最适合函数选定这些个体。
     *	@param population
     *	@return
     */
    public static Individual[] selection(Individual[] population) {
        Individual[] selected = new Individual[population.length / 2];
        for (int i = 0; i < selected.length; i++) {
            selected[i] = new Individual(population[i]);
        }
        return selected;
    }

    /**
     *  接收一代中被选定的个体作为参数，并且使用交叉操作生成下一代的种群。
     *	@param selected
     *	@param numberOfIndividuals 下一代的个体数目
     *	@param size 个体的染色体数目
     *	@return
     */
    public static Individual[] crossover(Individual[] selected, int numberOfIndividuals, int size) {
        Individual[] population = new Individual[numberOfIndividuals];
        Random random = new Random(System.nanoTime());
        for (int i = 0; i < numberOfIndividuals / 2; i++) {
            population[2 * i] = new Individual(size);
            population[2 * i + 1] = new Individual(size);
            int p1Index = random.nextInt(selected.length);
            int p2Index;
            do {
                p2Index = random.nextInt(selected.length);
            } while (p1Index == p2Index);
            Individual parent1 = selected[p1Index];
            Individual parent2 = selected[p2Index];
            crossover(parent1, parent2, population[2*i], population[2*i+1]);
        }
        return population;
    }
    
    /**
     *  实现交叉操作， 使用 parent1 个体和 parent2 个体生成下一代的 individual1 个体和 individual2 个体。
     *	@param parent1
     *	@param parent2
     *	@param individual1
     *	@param individual2
     */
    public static void crossover(final Individual parent1, final Individual parent2, final Individual individual1,
        final Individual individual2) {
        List<Integer> p1 = Arrays.asList(parent1.getChromosomes());
        List<Integer> p2 = Arrays.asList(parent2.getChromosomes());
        List<Integer> ch1 = new ArrayList<Integer>(p1.size());
        List<Integer> ch2 = new ArrayList<Integer>(p1.size());
        int size = p1.size();

        Random random = new Random();

        int number1 = random.nextInt(size - 1);
        int number2;
        
        do {
            number2 = random.nextInt(size);
        } while (number2==number1);

        int start = Math.min(number1, number2);
        int end = Math.max(number1, number2);
        ch1.addAll(p1.subList(start, end));
        ch2.addAll(p2.subList(start, end));

        int currentCity = 0;
        int currentCityParent1 = 0;
        int currentCityParent2 = 0;
        for (int i = 0; i < size; i++) {

            currentCity = (end + i) % size;

            currentCityParent1 = p1.get(currentCity);
            currentCityParent2 = p2.get(currentCity);

            if (!ch1.contains(currentCityParent2)) {
                ch1.add(currentCityParent2);
            } 

            if (!ch2.contains(currentCityParent1)) {
                ch2.add(currentCityParent1);
            } 
        }

        Collections.rotate(ch1, start);
        Collections.rotate(ch2, start);
        individual1.setChromosomes(ch1.toArray(individual1.getChromosomes()));
        individual2.setChromosomes(ch2.toArray(individual2.getChromosomes()));
    }
    
    /**
     *  使用参数中接收的距离矩阵，将适应度函数应用到种群的全部个体。
     *	@param population
     *	@param distanceMatrix
     */
    public static void evaluate(Individual[] population, int[][] distanceMatrix) {
        for (Individual individual : population) {
            evaluate(individual, distanceMatrix);
        }
        Arrays.sort(population);
    }
    
    /**
     *  将适应度函数应用到某个体。
     *	@param individual
     *	@param distanceMatrix
     */
    public static void evaluate(Individual individual, int[][] distanceMatrix) {
        Integer chromosomes[] = individual.getChromosomes();
        int totalDistance = 0;
        int source, destination;

        for (int i = 0; i < chromosomes.length - 1; i++) {
            source = chromosomes[i];
            destination = chromosomes[i + 1];
            totalDistance += distanceMatrix[source][destination];
        }
        source = chromosomes[chromosomes.length - 1];
        destination = chromosomes[0];
        totalDistance += distanceMatrix[source][destination];

        individual.setValue(totalDistance);
    }
}
