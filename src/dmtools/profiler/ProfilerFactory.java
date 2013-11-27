/*
 * Credit to Rob Simutis
 */
 
package dmtools.profiler;


/**
*  Factory object to create instances of <code>Profiler</code>s.
*  There are currently three types of <code>Profiler</code>s
*  available; a Thread-aware profiler, a profiler that is not Thread-aware,
*  and a silent profiler.
*  <p>
*  By default, profiling is disabled; any calls to a profiler from this factory
*  will be ignored.  When profiling is enabled, by default, the profiler is Thread-aware.
*  A Thread-aware profiler is aware of which thread calls each of its
*  <code>startTask(String)</code> and <code>stopTask(String)</code> methods
*  and will track accordingly.  However, there is additional overhead in keeping
*  track of the separate threads, which slightly increases the overall profile.
*  A <code>Profiler</code> that is not Thread-aware will have less overhead for
*  each <code>startTask(String)</code> and <code>stopTask(String)</code>
*  invocation, but should only be used in a context where they have little-to-no
*  chance of being used concurrently by more than one thread.
*  
*/
public class ProfilerFactory {

    /**
    *  Property Name:  <code>dmtools.profiler.enabled</code>
    *  <p>
    *  Boolean property to define whether or not profiling is enabled on the system.
    *  When this value is passed in through the command-line (e.g. <code>-Ddmtools.profiler=true</code>),
    *  profiling is active.  If the value is not passed, a silent profiler which
    *  ignores all method calls is used.
    *  <p>
    *  This property has been set as a command-line property under the theory that
    *  it will cause the end-user to do some work in order to enable profiling.
    *  If this property came from either the command-line or the fwm.properties file,
    *  it is more likely that a properties file containing this property would be
    *  promoted to an environment where profiling should <b>not</b> take place.
    */
    public static final String PROFILING_ENABLED_PROPERTY = "fwm.profiler.enabled";
    
    /**
    *  Property Name:  <code>dmtools.profiler.thread.aware</code>
    *  <p>
    *  Boolean property to define whether or not the <code>Profiler</code>
    *  returned is aware of which Thread is calling its methods.
    *  This property must be defined on the command-line 
    *  <p>
    *  Note that the default value for this property is true and must explicitely be 
    *  set to the <code>String "false"</code>.
    */
    public static final String THREAD_AWARE_PROPERTY = "dmtools.profiler.thread.aware";
    
    
    // Note: These three are hardcoded classnames such that the profiling classes can be removed
    // from the hierarchy without causing an exception on class load.
    protected static final String THREAD_AWARE_PROFILER_CLASSNAME = "dmtools.profiler.ThreadAwareProfilerProxy";
    protected static final String SILENT_PROFILER_CLASSNAME = "dmtools.profiler.SilentProfilerImpl";
    protected static final String PROFILER_CLASSNAME = "dmtools.profiler.ProfilerImpl";
    
    private static ProfilerFactory instance = null;
    
    private boolean isThreadAware = true;
    private boolean isProfilingEnabled = false;

    
    /**
    *  Main constructor -- singleton class.
    */
    private ProfilerFactory() { 
    }
    
    /**
    *  Load properties used in constructing <code>Profiler</code>s.
    */
    protected void initialize() {
        isProfilingEnabled = Boolean.getBoolean(PROFILING_ENABLED_PROPERTY);
        // Check the command-line parameter (-D) 
        String isThreadAwareProperty = System.getProperty(THREAD_AWARE_PROPERTY);
     
        // Note:  Explicitely not creating a new Boolean() object; if the string is
        // null, Boolean defaults to false.  However, I'd like it to default to true
        // unless explicitely stated.        
        isThreadAware = (!"false".equals(isThreadAwareProperty));
        
            System.out.println("dmtools.profiler.ProfilerFactoy: Using properties:");
            System.out.println("dmtools.profiler.ProfilerFactoy: " + PROFILING_ENABLED_PROPERTY + "=" + isProfilingEnabled);
            System.out.println("dmtools.profiler.ProfilerFactoy: " + THREAD_AWARE_PROPERTY + "=" + isThreadAware);
    }
    
    /**
    *  Return an instance of the <code>ProfilerFactory</code>.
    */
    public static ProfilerFactory getInstance() {
        if (instance == null) {
            synchronized (ProfilerFactory.class) {
                if (instance == null) {
                    instance = new ProfilerFactory();
                    instance.initialize();
                }       
            }
        }

        return instance;
    }
    
    /**
    *  Indicates whether or not the factory is configured to produce 
    *  profilers which are Thread-aware.
    */
    public boolean isThreadAware() {
        return isThreadAware;
    }
    
    /**
    *  Indicates whether or not the profiling is enabled
    *  in the application; this generally means that profilers produced
    *  here will take any action when any of their methods are called only
    *  if profiling is enabled.
    */
    public boolean isProfilingEnabled() {
        return isProfilingEnabled;
    }
    
    
    /**
    *  Factory method which returns a new configured <code>Profiler</code> instance.
    */
    public Profiler createProfiler() {
        Profiler profiler = null;
        
        String profilerClassname = null;
        if (isProfilingEnabled) {
            if (isThreadAware) {
                profilerClassname = THREAD_AWARE_PROFILER_CLASSNAME;
            } else {
                profilerClassname = PROFILER_CLASSNAME;
            }
        } else {
            profilerClassname = SILENT_PROFILER_CLASSNAME;
        }
        
         try {
            Object o = Class.forName(profilerClassname).newInstance();
            profiler = (Profiler)o;
        } catch (Exception e) {
            System.out.println("dmtools.profiler.ProfilerFactoy: Exception while creating Profiler for class name : " + profilerClassname + "; returning null Profiler" + e);
            e.printStackTrace();
        }
        
        return profiler;
    }
 
    public void enableProfiling() {
        isProfilingEnabled = true;
    }
    
    public void disableProfiling() {
        isProfilingEnabled = false;
    }
}
