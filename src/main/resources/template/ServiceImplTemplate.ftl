package ${packagePrefix}.${suffixLow}.impl;

import ${packagePrefix}.${modelTargetPackage}.${Model};
import ${packagePrefix}.${modelTargetPackage}.${Model}Example;
<#if PK != "String" && PK != "Integer">import ${packagePrefix}.${modelTargetPackage}.${PK};</#if>
import ${packagePrefix}.${daoTargetPackage}.${Model}Mapper;
import ${packagePrefix}.${daoTargetPackage}.MyBatisBaseDao;
import ${packagePrefix}.${suffixLow}.${Model}${suffix};
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author auto generator
*/
@Service
public class ${Model}${suffix}Impl extends Abstract${suffix}Impl<${Model}, ${PK}, ${Model}Example> implements ${Model}${suffix} {

    private ${Model}Mapper mapper;

    @Override
    protected MyBatisBaseDao<${Model}, ${PK}, ${Model}Example> getMapper() {
        return mapper;
    }

    @Autowired
    public void setMapper(${Model}Mapper mapper) {
        this.mapper = mapper;
    }
}
