import com.nomagic.magicdraw.properties.DateTimeProperty
import groovy.json.JsonOutput

def constructors = DateTimeProperty.class.getConstructors()
def sigs = constructors.collect { c -> 
    c.getName() + "(" + c.getParameterTypes().collect{it.getSimpleName()}.join(", ") + ")"
}

return JsonOutput.toJson([status: "OK", constructors: sigs])
