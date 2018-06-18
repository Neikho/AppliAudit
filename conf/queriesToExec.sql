/* ==========================================================================================================================
 Contains queries that will be executed on the database for the audit.

 Author : Alba Thomas (All4it)

 Creation date : 2018 June 7th

 Revisions
 ------------------------------------------------------------------------------------------------------------------------
 Version | Date       | Author                            | Comments
 ------------------------------------------------------------------------------------------------------------------------
 1.0     | 2018/06/07 | Alba Thomas (All4it)              | Initial version

 ------------------------------------------------------------------------------------------------------------------------
 ========================================================================================================================== */

#[Q1] SELECT name FROM v$database;
#[Q2] select round(total_size/1024,2) "TOTAL GB", round(system_size/1024,2) "SYSTEM GB", round(system_free/1024,2) "SYSTEM FREE GB", round(temp_size/1024,2) "TEMP GB", round(undo_size/1024,2) "UNDO GB", round(data_size/1024,2) "DATA GB", round(data_free/1024,2) "DATA FREE GB", pct_libre_utile "DATA PCT FREE" from (with cible as (select d.name||'\@'||i.host_name as env_name, to_char(sysdate, 'YYYY/MM/DD') as day from v$database d, v$instance i), ts_info as (select tablespace_name, decode(tablespace_name, 'SYSTEM', 'SYSTEM', 'SYSAUX', 'SYSTEM', contents) as contents_type from dba_tablespaces), ts_size as (select tablespace_name, sum(bytes) as size_b from dba_data_files group by tablespace_name union all select tablespace_name, sum(bytes) as size_b from dba_temp_files group by tablespace_name), ts_free as (select t.tablespace_name, sum(nvl(fs.bytes,0)) as size_b FROM dba_tablespaces t left outer join dba_free_space fs on t.tablespace_name = fs.tablespace_name group by t.tablespace_name), sum_size as (select round(sum(decode(ts_info.contents_type, 'PERMANENT', size_b, 0))/1024/1024,0) as data_size, round(sum(decode(ts_info.contents_type, 'TEMPORARY', size_b, 0))/1024/1024,0) as temp_size, round(sum(decode(ts_info.contents_type, 'UNDO', size_b, 0))/1024/1024,0) as undo_size, round(sum(decode(ts_info.contents_type, 'SYSTEM', size_b, 0))/1024/1024,0) as system_size, round(sum(size_b)/1024/1024,0) as total_size from ts_info natural join ts_size), sum_free as (select round(sum(decode(ts_info.contents_type, 'PERMANENT', size_b, 0))/1024/1024,0) as data_free, round(sum(decode(ts_info.contents_type, 'TEMPORARY', 0, 0))/1024/1024,0) as temp_free, round(sum(decode(ts_info.contents_type, 'UNDO', size_b, 0))/1024/1024,0) as undo_free, round(sum(decode(ts_info.contents_type, 'SYSTEM', size_b, 0))/1024/1024,0) as system_free, round(sum(size_b)/1024/1024,0) as total_free from ts_info natural join ts_free) select cible.env_name, cible.day, data_size, data_free, temp_size, temp_free, undo_size, undo_free, system_size, system_free, total_size, total_free, (system_size + data_size + undo_size + temp_size) as enveloppe_totale, (data_size) as enveloppe_utile, (data_size - data_free) as utilise_utile, (data_free) as libre_utile, round(100*(data_free) / (data_size),0) as pct_libre_utile from cible cross join sum_size cross join sum_free);
#[Q3] select sid, serial#, username, status, machine, program, process, last_call_et, event from v$session where username is not null order by sid;
#[Q4] select machine, count(*) from v$session group by machine order by machine;
#[Q5] select 100 * (1-(pr.value/(bg.value + cg.value))) from v$sysstat pr, v$sysstat bg, v$sysstat cg where pr.name='physical reads' and bg.name='db block gets'  and cg.name ='consistent gets';
#[Q6] select value from v$pgastat where name='cache hit percentage';
#[Q7] SELECT count(*) FROM v$locked_object l, all_objects t, v$session s WHERE l.object_id=t.object_id and s.sid=l.session_id;
#[Q8] SELECT s.username, s.machine, s.program, t.owner, t.object_type, t.object_name, l.locked_mode, s.sid, o.ctime FROM v$locked_object l, all_objects t, v$session s, v$lock o WHERE l.object_id=t.object_id and s.sid=l.session_id and o.sid=l.session_id and o.id1=l.object_id order by o.ctime desc;
#[Q9] SELECT * FROM csav_mdl.combo_chk;
#[Q10] SELECT id_banque, code_banque, nom, nom_inter, adresse_1, adresse_2, code_postal, ville FROM csav_mdl.banques WHERE rownum < 5;
