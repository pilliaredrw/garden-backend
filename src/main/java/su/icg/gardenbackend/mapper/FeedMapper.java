package su.icg.gardenbackend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import su.icg.gardenbackend.entity.Feed;

@Mapper
public interface FeedMapper extends BaseMapper<Feed> {
    // extends BaseMapper 之后，自动拥有了 insert, delete, update, selectById, selectList 等所有方法。
}