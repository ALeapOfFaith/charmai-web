package com.charmai.miniapp.api;

import com.charmai.common.core.domain.R;
import com.charmai.miniapp.entity.GpuNodeEntity;
import com.charmai.miniapp.queue.GpuAddressPool;
import com.charmai.miniapp.scheduler.DataLoader;
import com.charmai.miniapp.service.GpuNodeService;
import com.charmai.miniapp.service.impl.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * @author huangyicao
 * @email huangyicao666@gmail.com
 * @date 2023-07-26 18:19:34
 */
@RestController
@RequestMapping("generator/gpunode")
public class GpuNodeController {
    @Autowired
    private GpuNodeService gpuNodeService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params) {
        PageUtils page = gpuNodeService.queryPage(params);

        return R.ok(page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id) {
        GpuNodeEntity gpuNode = gpuNodeService.getById(id);

        return R.ok(gpuNode);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody GpuNodeEntity gpuNode) {
        gpuNode.setStatus(0);
        gpuNode.setCreatTime(new Date());
        gpuNodeService.save(gpuNode);
        DataLoader.gpuAddressPool.addLoraIp(gpuNode.getAddress());
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/save/list")
    public R saveList(@RequestBody List<String> addressList) {
        for (String address : addressList) {
            GpuNodeEntity gpuNode = new GpuNodeEntity();
            gpuNode.setAddress(address);
            gpuNode.setStatus(0);
            gpuNode.setCreatTime(new Date());
            gpuNodeService.save(gpuNode);
            DataLoader.gpuAddressPool.addLoraIp(gpuNode.getAddress());
        }
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody GpuNodeEntity gpuNode) {
        gpuNodeService.updateById(gpuNode);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody List<String> addressList) {
        for (String address : addressList) {
            GpuNodeEntity nodeByAddress = gpuNodeService.getNodeByAddress(address);
            if (nodeByAddress != null) {
                nodeByAddress.setStatus(1);
                gpuNodeService.updateById(nodeByAddress);
            }
            if (DataLoader.gpuAddressPool.containsLoraIp(address)) {
                DataLoader.gpuAddressPool.removeLoraIp(address);
            }
            if (DataLoader.gpuAddressPool.containsIp(address)) {
                DataLoader.gpuAddressPool.removeIP(address);
            }

        }
        return R.ok();
    }

    /**
     * 保存
     */
    @RequestMapping("/refresh")
    public R refresh() {
        DataLoader.gpuAddressPool.removeAllIP();
        DataLoader.gpuAddressPool.removeAllLoraIP();
        List<GpuNodeEntity> gpuNodeList = gpuNodeService.queryAllAvailable();
        int i = 0;
        if (gpuNodeList != null && gpuNodeList.size() > 0) {
            if (gpuNodeList.size() < 5) {
                for (GpuNodeEntity gpuNodeEntity : gpuNodeList) {
                    DataLoader.gpuAddressPool.addLoraIp(gpuNodeEntity.getAddress());
                }
            } else {
                for (GpuNodeEntity gpuNodeEntity : gpuNodeList) {
                    if (i < 3) {
                        DataLoader.gpuAddressPool.addIP(gpuNodeEntity.getAddress());
                        i++;
                    } else {
                        DataLoader.gpuAddressPool.addLoraIp(gpuNodeEntity.getAddress());
                    }
                }
            }
        }
        return R.ok();
    }

    @RequestMapping("/cache/node/address")
    public R getCacheNodeAddress() {
        return R.ok(DataLoader.gpuAddressPool.getCacheNode());
    }


}
