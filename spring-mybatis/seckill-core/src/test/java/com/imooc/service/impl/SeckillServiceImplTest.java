package com.imooc.service.impl;

import com.imooc.BaseTest;
import com.imooc.dto.SeckillExecution;
import com.imooc.exception.SeckillCloseException;
import org.junit.Test;
import com.imooc.dto.Exposer;
import com.imooc.entity.Seckill;
import com.imooc.exception.RepeatKillException;
import com.imooc.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class SeckillServiceImplTest extends BaseTest {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private SeckillService seckillService;

	@Test
	public void testGetSeckillList() throws Exception {
		List<Seckill> list = seckillService.getSeckillList();
		logger.info("list={}", list);
	}

	@Test
	public void testGetById() throws Exception {
		long id = 1000;
		Seckill seckill = seckillService.getById(id);
		logger.info("seckill={}", seckill);
	}

	// 测试代码完整逻辑，注意可重复执行
	@Test
	public void testSeckillLogic() throws Exception {
		long id = 1001;
		Exposer exposer = seckillService.exportSeckillUrl(id);
		if (exposer.isExposed()) {
			logger.info("exposer={}", exposer);
			long phone = 13631231235L;
			String md5 = exposer.getMd5();
			try {
				SeckillExecution execution =  seckillService.executeSeckill(id, phone, md5);
				logger.info("execution={}", execution);
			} catch (RepeatKillException e) {
				logger.error(e.getMessage());
			} catch (SeckillCloseException e) {
				logger.error(e.getMessage());
			}
		} else {
			// 秒杀未开启
			logger.error("exposer={}", exposer);
		}
	}

	@Test
	public void testExecuteSeckillProcedure() throws Exception {
		long seckillId = 1001;
		long phone = 13631231234L;
		Exposer exposer = seckillService.exportSeckillUrl(seckillId);
		if (exposer.isExposed()) {
			String md5 = exposer.getMd5();
			SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId, phone, md5);
			logger.info(execution.getStateInfo());
		}
	}

}
