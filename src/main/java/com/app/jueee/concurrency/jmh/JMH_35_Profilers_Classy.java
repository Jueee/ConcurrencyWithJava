package com.app.jueee.concurrency.jmh;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.profile.ClassloaderProfiler;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;


/**
 *  使用Profiler作测试
 *	
 *	@author hzweiyongqiang
 */
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(3)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
public class JMH_35_Profilers_Classy {
    


    /**
     * Our own crippled classloader, that can only load a simple class over and over again.
     */
    public static class XLoader extends URLClassLoader {
        private static final byte[] X_BYTECODE = new byte[]{
                (byte) 0xCA, (byte) 0xFE, (byte) 0xBA, (byte) 0xBE, 0x00, 0x00, 0x00, 0x34, 0x00, 0x0D, 0x0A, 0x00, 0x03, 0x00,
                0x0A, 0x07, 0x00, 0x0B, 0x07, 0x00, 0x0C, 0x01, 0x00, 0x06, 0x3C, 0x69, 0x6E, 0x69, 0x74, 0x3E, 0x01, 0x00, 0x03,
                0x28, 0x29, 0x56, 0x01, 0x00, 0x04, 0x43, 0x6F, 0x64, 0x65, 0x01, 0x00, 0x0F, 0x4C, 0x69, 0x6E, 0x65, 0x4E, 0x75,
                0x6D, 0x62, 0x65, 0x72, 0x54, 0x61, 0x62, 0x6C, 0x65, 0x01, 0x00, 0x0A, 0x53, 0x6F, 0x75, 0x72, 0x63, 0x65, 0x46,
                0x69, 0x6C, 0x65, 0x01, 0x00, 0x06, 0x58, 0x2E, 0x6A, 0x61, 0x76, 0x61, 0x0C, 0x00, 0x04, 0x00, 0x05, 0x01, 0x00,
                0x01, 0x58, 0x01, 0x00, 0x10, 0x6A, 0x61, 0x76, 0x61, 0x2F, 0x6C, 0x61, 0x6E, 0x67, 0x2F, 0x4F, 0x62, 0x6A, 0x65,
                0x63, 0x74, 0x00, 0x20, 0x00, 0x02, 0x00, 0x03, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01, 0x00, 0x00, 0x00, 0x04, 0x00,
                0x05, 0x00, 0x01, 0x00, 0x06, 0x00, 0x00, 0x00, 0x1D, 0x00, 0x01, 0x00, 0x01, 0x00, 0x00, 0x00, 0x05, 0x2A,
                (byte) 0xB7, 0x00, 0x01, (byte) 0xB1, 0x00, 0x00, 0x00, 0x01, 0x00, 0x07, 0x00, 0x00, 0x00, 0x06, 0x00, 0x01, 0x00,
                0x00, 0x00, 0x01, 0x00, 0x01, 0x00, 0x08, 0x00, 0x00, 0x00, 0x02, 0x00, 0x09,
        };

        public XLoader() {
            super(new URL[0], ClassLoader.getSystemClassLoader());
        }

        @Override
        protected Class<?> findClass(final String name) throws ClassNotFoundException {
            return defineClass(name, X_BYTECODE, 0, X_BYTECODE.length);
        }

    }

    @Benchmark
    public Class<?> load() throws ClassNotFoundException {
        return Class.forName("X", true, new XLoader());
    }

    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMH_35_Profilers_Classy.class.getSimpleName())
                .addProfiler(ClassloaderProfiler.class)
//              .addProfiler(CompilerProfiler.class)
                .build();

        new Runner(opt).run();
    }
    
    /**
     * 
# Run complete. Total time: 00:00:31

Benchmark                                        Mode  Cnt      Score       Error        Units
JMH_35_Profilers_Classy.load                     avgt   15  76709.656 ±  6818.505        ns/op
JMH_35_Profilers_Classy.load:·class.load         avgt   15  13141.188 ±  1073.583  classes/sec
JMH_35_Profilers_Classy.load:·class.load.norm    avgt   15      1.000 ±     0.001   classes/op
JMH_35_Profilers_Classy.load:·class.unload       avgt   15   8078.285 ± 16287.787  classes/sec
JMH_35_Profilers_Classy.load:·class.unload.norm  avgt   15      0.695 ±     1.433   classes/op
     * 
     */
}
