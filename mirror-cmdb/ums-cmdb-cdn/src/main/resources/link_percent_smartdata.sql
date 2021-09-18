SELECT
	jieru.province_name 省份,
	(ssh.province_ssh / jieru.province_jieru) SSH连通率,
	(agent.province_agent / jieru.province_jieru) Agent连通率,
	(ssh.province_ssh - ssh.fh_ssh)/(jieru.province_jieru-jieru.fh_jieru) SSH连通率（不含烽火）,
	(agent.province_agent-agent.fh_agent)/(jieru.province_jieru-jieru.fh_jieru) Agent连通率（不含烽火）,
	(ssh.hw_ssh / jieru.hw_jieru) SSH连通率（华为）,
	(ssh.zx_ssh / jieru.zx_jieru) SSH连通率（中兴）,
	(ssh.fh_ssh / jieru.fh_jieru) SSH连通率（烽火）,
	(ssh.hy_ssh / jieru.hy_jieru) SSH连通率（杭研）,
	(agent.hw_agent / jieru.hw_jieru) Agent连通率（华为）,
	(agent.zx_agent / jieru.zx_jieru) Agent连通率（中兴）,
	(agent.fh_agent / jieru.fh_jieru) Agent连通率（烽火）,
	(agent.hy_agent / jieru.hy_jieru) Agent连通率（杭研）,
	jieru.hw_jieru ,
	jieru.zx_jieru ,
	jieru.fh_jieru ,
	jieru.hy_jieru ,
	oms.hw_oms ,
	oms.zx_oms ,
	oms.fh_oms ,
	oms.hy_oms ,
	ssh.hw_ssh ,
	ssh.zx_ssh ,
	ssh.fh_ssh ,
	ssh.hy_ssh ,
	agent.hw_agent ,
	agent.zx_agent ,
	agent.fh_agent ,
	agent.hy_agent 
FROM
	(
		SELECT
			province_jieru.province_name,
			IFNULL(province_jieru.jieru, 0) province_jieru,
			IFNULL(hw_jieru.jieru, 0) hw_jieru,
			IFNULL(zx_jieru.jieru, 0) zx_jieru,
			IFNULL(fh_jieru.jieru, 0) fh_jieru,
			IFNULL(hy_jieru.jieru, 0) hy_jieru
		FROM
			(
				SELECT
					p.NAME province_name,
					count(ds.salt_status) jieru
				FROM
					v_jt_opsapp_deviceserver ds
				INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
				INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
				INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
				INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
				INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
				WHERE ds.salt_status = 4
				GROUP BY p.NAME
			) province_jieru
		LEFT JOIN (
			SELECT
				p.NAME province_name,
				fl.NAME flat,
				count(ds.salt_status) jieru
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			WHERE ds.salt_status = 4
			  AND ds.flat_id = 4
			GROUP BY p.NAME, fl.NAME
		) fh_jieru ON province_jieru.province_name = fh_jieru.province_name
		LEFT JOIN (
			SELECT
				p.NAME province_name,
				fl.NAME flat,
				count(ds.salt_status) jieru
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			WHERE ds.salt_status = 4 
			  AND ds.flat_id = 3
			GROUP BY p.NAME, fl.NAME
		) zx_jieru ON province_jieru.province_name = zx_jieru.province_name
		LEFT JOIN (
			SELECT
				p.NAME province_name,
				fl.NAME flat,
				count(ds.salt_status) jieru
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			WHERE ds.salt_status = 4
			  AND ds.flat_id = 2
			GROUP BY p.NAME, fl.NAME
		) hw_jieru ON province_jieru.province_name = hw_jieru.province_name
		LEFT JOIN (
			SELECT
				p.NAME province_name,
				fl.NAME flat,
				count(ds.salt_status) jieru
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			WHERE ds.salt_status = 4
			  AND ds.flat_id = 1
			GROUP BY p.NAME, fl.NAME
		) hy_jieru ON province_jieru.province_name = hy_jieru.province_name
	) jieru
LEFT JOIN (
	SELECT
		province_oms.province_name,
		IFNULL(province_oms.oms, 0) province_oms,
		IFNULL(hw_oms.oms, 0) hw_oms,
		IFNULL(zx_oms.oms, 0) zx_oms,
		IFNULL(fh_oms.oms, 0) fh_oms,
		IFNULL(hy_oms.oms, 0) hy_oms
	FROM
		(
			SELECT
				p.NAME province_name,
				count(ds.soft_status) oms
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			WHERE ds.salt_status = 4 
			  AND ds.soft_status = 3
			GROUP BY p.NAME
		) province_oms
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) oms
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		WHERE ds.salt_status = 4 
		  AND ds.soft_status = 3
		  AND ds.flat_id = 4
		GROUP BY p.NAME, fl.NAME
	) fh_oms ON province_oms.province_name = fh_oms.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) oms
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		WHERE ds.salt_status = 4 
		  AND ds.soft_status = 3
		  AND ds.flat_id = 3
		GROUP BY p.NAME, fl.NAME
	) zx_oms ON province_oms.province_name = zx_oms.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) oms
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		WHERE ds.salt_status = 4 
		  AND ds.soft_status = 3
		  AND ds.flat_id = 2
		GROUP BY p.NAME, fl.NAME
	) hw_oms ON province_oms.province_name = hw_oms.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) oms
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		WHERE ds.salt_status = 4 
          AND ds.soft_status = 3
		  AND ds.flat_id = 1
		GROUP BY p.NAME, fl.NAME
	) hy_oms ON province_oms.province_name = hy_oms.province_name
) oms ON jieru.province_name = oms.province_name
LEFT JOIN (
	SELECT
		province_ssh.province_name,
		IFNULL(province_ssh.ssh, 0) province_ssh,
		IFNULL(hw_ssh.ssh, 0) hw_ssh,
		IFNULL(zx_ssh.ssh, 0) zx_ssh,
		IFNULL(fh_ssh.ssh, 0) fh_ssh,
		IFNULL(hy_ssh.ssh, 0) hy_ssh
	FROM
		(
			SELECT
				p.NAME province_name,
				count(conn.ssh_status) ssh
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			INNER JOIN v_jt_opsapp_deviceconnective conn ON conn.device_server_id = ds.id AND conn.ssh_status = 0
			WHERE ds.salt_status = 4
			GROUP BY p.NAME
		) province_ssh
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(conn.ssh_status) ssh
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_opsapp_deviceconnective conn ON conn.device_server_id = ds.id AND conn.ssh_status = 0
		WHERE ds.salt_status = 4 
		  AND ds.flat_id = 4
		GROUP BY p.NAME, fl.NAME
	) fh_ssh ON province_ssh.province_name = fh_ssh.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(conn.ssh_status) ssh
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_opsapp_deviceconnective conn ON conn.device_server_id = ds.id AND conn.ssh_status = 0
		WHERE ds.salt_status = 4 
		  AND ds.flat_id = 3
		GROUP BY p.NAME, fl.NAME
	) zx_ssh ON province_ssh.province_name = zx_ssh.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(conn.ssh_status) ssh
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_opsapp_deviceconnective conn ON conn.device_server_id = ds.id AND conn.ssh_status = 0
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 2
		GROUP BY p.NAME, fl.NAME
	) hw_ssh ON province_ssh.province_name = hw_ssh.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(conn.ssh_status) ssh
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_opsapp_deviceconnective conn ON conn.device_server_id = ds.id AND conn.ssh_status = 0
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 1
		GROUP BY p.NAME, fl.NAME
	) hy_ssh ON province_ssh.province_name = hy_ssh.province_name
) ssh ON jieru.province_name = ssh.province_name
LEFT JOIN (
	SELECT
		province_agent.province_name,
		IFNULL(province_agent.agent, 0) province_agent,
		IFNULL(hw_agent.agent, 0) hw_agent,
		IFNULL(zx_agent.agent, 0) zx_agent,
		IFNULL(fh_agent.agent, 0) fh_agent,
		IFNULL(hy_agent.agent, 0) hy_agent
	FROM
		(
			SELECT
				p.NAME province_name,
				count(ds.soft_status) agent
			FROM
				v_jt_opsapp_deviceserver ds
			INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
			INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
			INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
			INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
			INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
			INNER JOIN v_jt_zabbix_host zb ON ds.id = zb.id AND zb.zb_status = 1
			WHERE ds.salt_status = 4
			GROUP BY p.NAME
		) province_agent
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) agent
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_zabbix_host zb ON ds.id = zb.id AND zb.zb_status = 1
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 4
		GROUP BY p.NAME, fl.NAME
	) fh_agent ON province_agent.province_name = fh_agent.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) agent
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_zabbix_host zb ON ds.id = zb.id AND zb.zb_status = 1
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 3
		GROUP BY p.NAME, fl.NAME
	) zx_agent ON province_agent.province_name = zx_agent.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) agent
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_zabbix_host zb ON ds.id = zb.id AND zb.zb_status = 1
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 2
		GROUP BY p.NAME, fl.NAME
	) hw_agent ON province_agent.province_name = hw_agent.province_name
	LEFT JOIN (
		SELECT
			p.NAME province_name,
			fl.NAME flat,
			count(ds.soft_status) agent
		FROM
			v_jt_opsapp_deviceserver ds
		INNER JOIN v_jt_opsapp_machineroom mr ON mr.id = ds.machine_room_id
		INNER JOIN v_jt_opsapp_flat fl ON fl.id = ds.flat_id
		INNER JOIN v_jt_opsapp_district d ON d.id = mr.district_id
		INNER JOIN v_jt_opsapp_city c ON c.id = d.city_id
		INNER JOIN v_jt_opsapp_province p ON p.id = c.province_id
		INNER JOIN v_jt_zabbix_host zb ON ds.id = zb.id AND zb.zb_status = 1
		WHERE ds.salt_status = 4
		  AND ds.flat_id = 1
		GROUP BY p.NAME, fl.NAME
	) hy_agent ON province_agent.province_name = hy_agent.province_name
) agent ON jieru.province_name = agent.province_name