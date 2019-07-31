package bjfu.lixian.mapper;

import bjfu.lixian.pojo.SearchRecords;
import bjfu.lixian.utils.MyMapper;

import java.util.List;

public interface SearchRecordsMapper extends MyMapper<SearchRecords> {
    public List<String> getHotwords();

}