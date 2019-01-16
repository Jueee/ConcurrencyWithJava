package com.app.jueee.concurrency.jmh;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.security.Policy;
import java.security.URIParameter;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.TearDown;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  测试未用与使用SecurityManager时的性能
 *	
 *	@author hzweiyongqiang
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_33_SecurityManager {

    @State(Scope.Benchmark)
    public static class SecurityManagerInstalled {
        @Setup
        public void setup() throws IOException, NoSuchAlgorithmException, URISyntaxException {
            URI policyFile = JMH_33_SecurityManager.class.getResource("/jmh-security.policy").toURI();
            Policy.setPolicy(Policy.getInstance("JavaPolicy", new URIParameter(policyFile)));
            System.setSecurityManager(new SecurityManager());
        }

        @TearDown
        public void tearDown() {
            System.setSecurityManager(null);
        }
    }

    @State(Scope.Benchmark)
    public static class SecurityManagerEmpty {
        @Setup
        public void setup() throws IOException, NoSuchAlgorithmException, URISyntaxException {
            System.setSecurityManager(null);
        }
    }

    @Benchmark
    public String testWithSM(SecurityManagerInstalled s) throws InterruptedException {
        return System.getProperty("java.home");
    }

    @Benchmark
    public String testWithoutSM(SecurityManagerEmpty s) throws InterruptedException {
        return System.getProperty("java.home");
    }
    

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_33_SecurityManager.class.getSimpleName())
                .warmupIterations(5)
                .measurementIterations(5)
                .forks(1)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:03:21

Benchmark                             Mode  Cnt    Score    Error  Units
JMH_33_SecurityManager.testWithSM     avgt    5  945.916 ± 22.533  ns/op
JMH_33_SecurityManager.testWithoutSM  avgt    5    7.607 ±  0.090  ns/op
     * 
     */
}
