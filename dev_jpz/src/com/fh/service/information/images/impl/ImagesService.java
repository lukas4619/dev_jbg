package com.fh.service.information.images.impl;

import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.fh.dao.DaoSupport;
import com.fh.entity.Page;
import com.fh.util.PageData;
import com.fh.service.information.images.ImagesManager;

/** 
 * 说明： 图片库
 * 创建人：Lukas 18923798379
 * 创建时间：2016-08-19
 * @version
 */
@Service("imagesService")
public class ImagesService implements ImagesManager{

	@Resource(name = "daoSupport")
	private DaoSupport dao;
	
	/**新增
	 * @param pd
	 * @throws Exception
	 */
	public void save(PageData pd)throws Exception{
		dao.save("ImagesMapper.save", pd);
	}
	
	/**删除
	 * @param pd
	 * @throws Exception
	 */
	public void delete(PageData pd)throws Exception{
		dao.delete("ImagesMapper.delete", pd);
	}
	
	/**修改
	 * @param pd
	 * @throws Exception
	 */
	public void edit(PageData pd)throws Exception{
		dao.update("ImagesMapper.edit", pd);
	}
	
	/**修改上传微信服务器返回标识和状态
	 * @param pd
	 * @throws Exception
	 */
	public void editUpLoad(PageData pd)throws Exception{
		dao.update("ImagesMapper.editUpLoad", pd);
	}
	
	/**列表
	 * @param page
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> list(Page page)throws Exception{
		return (List<PageData>)dao.findForList("ImagesMapper.datalistPage", page);
	}
	
	/**列表(全部)
	 * @param pd
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PageData> listAll(PageData pd)throws Exception{
		return (List<PageData>)dao.findForList("ImagesMapper.listAll", pd);
	}
	
	/**通过id获取数据
	 * @param pd
	 * @throws Exception
	 */
	public PageData findById(PageData pd)throws Exception{
		return (PageData)dao.findForObject("ImagesMapper.findById", pd);
	}
	
	/**批量删除
	 * @param ArrayDATA_IDS
	 * @throws Exception
	 */
	public void deleteAll(String[] ArrayDATA_IDS)throws Exception{
		dao.delete("ImagesMapper.deleteAll", ArrayDATA_IDS);
	}

	@Override
	public void insertBatch(List<PageData> lp) throws Exception {
		dao.save("ImagesMapper.insertBatch", lp);
	}
	
}

