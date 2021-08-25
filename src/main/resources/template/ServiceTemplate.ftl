package ${packagePrefix}.${suffixLow};

import ${packagePrefix}.${modelTargetPackage}.${Model};
<#if PK != "String" && PK != "Integer">import ${packagePrefix}.${modelTargetPackage}.${PK};</#if>
import ${packagePrefix}.${modelTargetPackage}.${Model}Example;

/**
* @author auto generator
*/
public interface ${Model}${suffix} extends Base${suffix}<${Model}, ${PK}, ${Model}Example> {
}
