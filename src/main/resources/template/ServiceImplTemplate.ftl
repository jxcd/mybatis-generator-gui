package ${packagePrefix}.${suffixLow}.impl;

import ${modelTargetPackage}.${Model};
import ${modelTargetPackage}.${Model}Example;
import ${daoTargetPackage}.${Model}Mapper;
import ${daoTargetPackage}.MyBatisBaseDao;
import ${packagePrefix}.${suffixLow}.${Model}Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ${Model}${suffix}Impl extends Abstract${suffix}Impl<${Model}, ${PK}, ${Model}Example> implements ${Model}${suffix} {
    @Autowired
    private ${Model}Mapper mapper;

    @Override
    protected MyBatisBaseDao<${Model}, ${PK}, ${Model}Example> getMapper() {
        return mapper;
    }
}
