#[Q1] SELECT name FROM v$database;
#[Q2] SELECT * FROM csav_mdl.combo_chk;
#[Q7] SELECT ID_BANQUE,	CODE_BANQUE,	NOM,	NOM_INTER,	ADRESSE_1,	ADRESSE_2,	CODE_POSTAL,	VILLE,	ID_PAYS	 FROM csav_mdl.banques 
WHERE ROWNUM < 5;
#[Q9] SELECT 1 FROM dual;
#[Q4] SELECT cc_label FROM csav_mdl.combo_chk;
#[Q5] select round(total_size/1024,2) "TOTAL GB", round(system_size/1024,2) "SYSTEM GB", round(system_free/1024,2) "SYSTEM FREE GB", round(temp_size/1024,2) "TEMP GB", round(undo_size/1024,2) "UNDO GB", round(data_size/1024,2) "DATA GB", round(data_free/1024,2) "DATA FREE GB", pct_libre_utile "DATA PCT FREE" from (with cible as (select d.name||'\@'||i.host_name as env_name, to_char(sysdate, 'YYYY/MM/DD') as day from v$database d, v$instance i), ts_info as (select tablespace_name, decode(tablespace_name, 'SYSTEM', 'SYSTEM', 'SYSAUX', 'SYSTEM', contents) as contents_type from dba_tablespaces), ts_size as (select tablespace_name, sum(bytes) as size_b from dba_data_files group by tablespace_name union all select tablespace_name, sum(bytes) as size_b from dba_temp_files group by tablespace_name), ts_free as (select t.tablespace_name, sum(nvl(fs.bytes,0)) as size_b FROM dba_tablespaces t left outer join dba_free_space fs on t.tablespace_name = fs.tablespace_name group by t.tablespace_name), sum_size as (select round(sum(decode(ts_info.contents_type, 'PERMANENT', size_b, 0))/1024/1024,0) as data_size, round(sum(decode(ts_info.contents_type, 'TEMPORARY', size_b, 0))/1024/1024,0) as temp_size, round(sum(decode(ts_info.contents_type, 'UNDO', size_b, 0))/1024/1024,0) as undo_size, round(sum(decode(ts_info.contents_type, 'SYSTEM', size_b, 0))/1024/1024,0) as system_size, round(sum(size_b)/1024/1024,0) as total_size from ts_info natural join ts_size), sum_free as (select round(sum(decode(ts_info.contents_type, 'PERMANENT', size_b, 0))/1024/1024,0) as data_free, round(sum(decode(ts_info.contents_type, 'TEMPORARY', 0, 0))/1024/1024,0) as temp_free, round(sum(decode(ts_info.contents_type, 'UNDO', size_b, 0))/1024/1024,0) as undo_free, round(sum(decode(ts_info.contents_type, 'SYSTEM', size_b, 0))/1024/1024,0) as system_free, round(sum(size_b)/1024/1024,0) as total_free from ts_info natural join ts_free) select cible.env_name, cible.day, data_size, data_free, temp_size, temp_free, undo_size, undo_free, system_size, system_free, total_size, total_free, (system_size + data_size + undo_size + temp_size) as enveloppe_totale, (data_size) as enveloppe_utile, (data_size - data_free) as utilise_utile, (data_free) as libre_utile, round(100*(data_free) / (data_size),0) as pct_libre_utile from cible cross join sum_size cross join sum_free);
#[Q3] select sql_id, sql_fulltext, executions, invalidations, parse_calls, buffer_gets, disk_reads, concurrency_wait_time, rows_processed, optimizer_mode, optimizer_cost, parsing_schema_name, module, cpu_time, elapsed_time from v$sqlarea where executions > 0 and parsing_schema_name not in ('SYS', 'SYSTEM', 'SYSMAN', 'WMSYS', 'OUTLN', 'CTXSYS', 'DBSNMP', 'XDB') and last_active_time > (sysdate - (1/(86400/60))) order by elapsed_time desc;
