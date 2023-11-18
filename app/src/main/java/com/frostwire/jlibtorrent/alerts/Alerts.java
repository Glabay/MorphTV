package com.frostwire.jlibtorrent.alerts;

import com.frostwire.jlibtorrent.swig.alert;
import com.frostwire.jlibtorrent.swig.libtorrent;

public final class Alerts {
    public static final int NUM_ALERT_TYPES = libtorrent.getNum_alert_types();
    private static CastLambda[] TABLE = buildTable();

    private interface CastLambda {
        Alert cast(alert alert);
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$1 */
    static class C06101 implements CastLambda {
        C06101() {
        }

        public Alert cast(alert alert) {
            return new TorrentAlert(alert.cast_to_torrent_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$2 */
    static class C06112 implements CastLambda {
        C06112() {
        }

        public Alert cast(alert alert) {
            return new PeerAlert(alert.cast_to_peer_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$3 */
    static class C06123 implements CastLambda {
        C06123() {
        }

        public Alert cast(alert alert) {
            return new TrackerAlert(alert.cast_to_tracker_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$4 */
    static class C06134 implements CastLambda {
        C06134() {
        }

        public Alert cast(alert alert) {
            return Alerts.handleUnknownAlert(alert);
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$5 */
    static class C06145 implements CastLambda {
        C06145() {
        }

        public Alert cast(alert alert) {
            return new TorrentRemovedAlert(alert.cast_to_torrent_removed_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$6 */
    static class C06156 implements CastLambda {
        C06156() {
        }

        public Alert cast(alert alert) {
            return new ReadPieceAlert(alert.cast_to_read_piece_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$7 */
    static class C06167 implements CastLambda {
        C06167() {
        }

        public Alert cast(alert alert) {
            return new FileCompletedAlert(alert.cast_to_file_completed_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$8 */
    static class C06178 implements CastLambda {
        C06178() {
        }

        public Alert cast(alert alert) {
            return new FileRenamedAlert(alert.cast_to_file_renamed_alert(alert));
        }
    }

    /* renamed from: com.frostwire.jlibtorrent.alerts.Alerts$9 */
    static class C06189 implements CastLambda {
        C06189() {
        }

        public Alert cast(alert alert) {
            return new FileRenameFailedAlert(alert.cast_to_file_rename_failed_alert(alert));
        }
    }

    private Alerts() {
    }

    public static Alert cast(alert alert) {
        return TABLE[alert.type()].cast(alert);
    }

    private static CastLambda[] buildTable() {
        CastLambda[] castLambdaArr = new CastLambda[NUM_ALERT_TYPES];
        castLambdaArr[0] = new C06101();
        castLambdaArr[1] = new C06112();
        castLambdaArr[2] = new C06123();
        castLambdaArr[3] = new C06134();
        castLambdaArr[4] = new C06145();
        castLambdaArr[5] = new C06156();
        castLambdaArr[6] = new C06167();
        castLambdaArr[7] = new C06178();
        castLambdaArr[8] = new C06189();
        castLambdaArr[9] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PerformanceAlert(alert.cast_to_performance_alert(alert));
            }
        };
        castLambdaArr[10] = new CastLambda() {
            public Alert cast(alert alert) {
                return new StateChangedAlert(alert.cast_to_state_changed_alert(alert));
            }
        };
        castLambdaArr[11] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TrackerErrorAlert(alert.cast_to_tracker_error_alert(alert));
            }
        };
        castLambdaArr[12] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TrackerWarningAlert(alert.cast_to_tracker_warning_alert(alert));
            }
        };
        castLambdaArr[13] = new CastLambda() {
            public Alert cast(alert alert) {
                return new ScrapeReplyAlert(alert.cast_to_scrape_reply_alert(alert));
            }
        };
        castLambdaArr[14] = new CastLambda() {
            public Alert cast(alert alert) {
                return new ScrapeFailedAlert(alert.cast_to_scrape_failed_alert(alert));
            }
        };
        castLambdaArr[15] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TrackerReplyAlert(alert.cast_to_tracker_reply_alert(alert));
            }
        };
        castLambdaArr[16] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtReplyAlert(alert.cast_to_dht_reply_alert(alert));
            }
        };
        castLambdaArr[17] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TrackerAnnounceAlert(alert.cast_to_tracker_announce_alert(alert));
            }
        };
        castLambdaArr[18] = new CastLambda() {
            public Alert cast(alert alert) {
                return new HashFailedAlert(alert.cast_to_hash_failed_alert(alert));
            }
        };
        castLambdaArr[19] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerBanAlert(alert.cast_to_peer_ban_alert(alert));
            }
        };
        castLambdaArr[20] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerUnsnubbedAlert(alert.cast_to_peer_unsnubbed_alert(alert));
            }
        };
        castLambdaArr[21] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerSnubbedAlert(alert.cast_to_peer_snubbed_alert(alert));
            }
        };
        castLambdaArr[22] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerErrorAlert(alert.cast_to_peer_error_alert(alert));
            }
        };
        castLambdaArr[23] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerConnectAlert(alert.cast_to_peer_connect_alert(alert));
            }
        };
        castLambdaArr[24] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerDisconnectedAlert(alert.cast_to_peer_disconnected_alert(alert));
            }
        };
        castLambdaArr[25] = new CastLambda() {
            public Alert cast(alert alert) {
                return new InvalidRequestAlert(alert.cast_to_invalid_request_alert(alert));
            }
        };
        castLambdaArr[26] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentFinishedAlert(alert.cast_to_torrent_finished_alert(alert));
            }
        };
        castLambdaArr[27] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PieceFinishedAlert(alert.cast_to_piece_finished_alert(alert));
            }
        };
        castLambdaArr[28] = new CastLambda() {
            public Alert cast(alert alert) {
                return new RequestDroppedAlert(alert.cast_to_request_dropped_alert(alert));
            }
        };
        castLambdaArr[29] = new CastLambda() {
            public Alert cast(alert alert) {
                return new BlockTimeoutAlert(alert.cast_to_block_timeout_alert(alert));
            }
        };
        castLambdaArr[30] = new CastLambda() {
            public Alert cast(alert alert) {
                return new BlockFinishedAlert(alert.cast_to_block_finished_alert(alert));
            }
        };
        castLambdaArr[31] = new CastLambda() {
            public Alert cast(alert alert) {
                return new BlockDownloadingAlert(alert.cast_to_block_downloading_alert(alert));
            }
        };
        castLambdaArr[32] = new CastLambda() {
            public Alert cast(alert alert) {
                return new UnwantedBlockAlert(alert.cast_to_unwanted_block_alert(alert));
            }
        };
        castLambdaArr[33] = new CastLambda() {
            public Alert cast(alert alert) {
                return new StorageMovedAlert(alert.cast_to_storage_moved_alert(alert));
            }
        };
        castLambdaArr[34] = new CastLambda() {
            public Alert cast(alert alert) {
                return new StorageMovedFailedAlert(alert.cast_to_storage_moved_failed_alert(alert));
            }
        };
        castLambdaArr[35] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentDeletedAlert(alert.cast_to_torrent_deleted_alert(alert));
            }
        };
        castLambdaArr[36] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentDeleteFailedAlert(alert.cast_to_torrent_delete_failed_alert(alert));
            }
        };
        castLambdaArr[37] = new CastLambda() {
            public Alert cast(alert alert) {
                return new SaveResumeDataAlert(alert.cast_to_save_resume_data_alert(alert));
            }
        };
        castLambdaArr[38] = new CastLambda() {
            public Alert cast(alert alert) {
                return new SaveResumeDataFailedAlert(alert.cast_to_save_resume_data_failed_alert(alert));
            }
        };
        castLambdaArr[39] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentPausedAlert(alert.cast_to_torrent_paused_alert(alert));
            }
        };
        castLambdaArr[40] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentResumedAlert(alert.cast_to_torrent_resumed_alert(alert));
            }
        };
        castLambdaArr[41] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentCheckedAlert(alert.cast_to_torrent_checked_alert(alert));
            }
        };
        castLambdaArr[42] = new CastLambda() {
            public Alert cast(alert alert) {
                return new UrlSeedAlert(alert.cast_to_url_seed_alert(alert));
            }
        };
        castLambdaArr[43] = new CastLambda() {
            public Alert cast(alert alert) {
                return new FileErrorAlert(alert.cast_to_file_error_alert(alert));
            }
        };
        castLambdaArr[44] = new CastLambda() {
            public Alert cast(alert alert) {
                return new MetadataFailedAlert(alert.cast_to_metadata_failed_alert(alert));
            }
        };
        castLambdaArr[45] = new CastLambda() {
            public Alert cast(alert alert) {
                return new MetadataReceivedAlert(alert.cast_to_metadata_received_alert(alert));
            }
        };
        castLambdaArr[46] = new CastLambda() {
            public Alert cast(alert alert) {
                return new UdpErrorAlert(alert.cast_to_udp_error_alert(alert));
            }
        };
        castLambdaArr[47] = new CastLambda() {
            public Alert cast(alert alert) {
                return new ExternalIpAlert(alert.cast_to_external_ip_alert(alert));
            }
        };
        castLambdaArr[48] = new CastLambda() {
            public Alert cast(alert alert) {
                return new ListenFailedAlert(alert.cast_to_listen_failed_alert(alert));
            }
        };
        castLambdaArr[49] = new CastLambda() {
            public Alert cast(alert alert) {
                return new ListenSucceededAlert(alert.cast_to_listen_succeeded_alert(alert));
            }
        };
        castLambdaArr[50] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PortmapErrorAlert(alert.cast_to_portmap_error_alert(alert));
            }
        };
        castLambdaArr[51] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PortmapAlert(alert.cast_to_portmap_alert(alert));
            }
        };
        castLambdaArr[52] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PortmapLogAlert(alert.cast_to_portmap_log_alert(alert));
            }
        };
        castLambdaArr[53] = new CastLambda() {
            public Alert cast(alert alert) {
                return new FastresumeRejectedAlert(alert.cast_to_fastresume_rejected_alert(alert));
            }
        };
        castLambdaArr[54] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerBlockedAlert(alert.cast_to_peer_blocked_alert(alert));
            }
        };
        castLambdaArr[55] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtAnnounceAlert(alert.cast_to_dht_announce_alert(alert));
            }
        };
        castLambdaArr[56] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtGetPeersAlert(alert.cast_to_dht_get_peers_alert(alert));
            }
        };
        castLambdaArr[57] = new CastLambda() {
            public Alert cast(alert alert) {
                return new StatsAlert(alert.cast_to_stats_alert(alert));
            }
        };
        castLambdaArr[58] = new CastLambda() {
            public Alert cast(alert alert) {
                return new CacheFlushedAlert(alert.cast_to_cache_flushed_alert(alert));
            }
        };
        castLambdaArr[59] = new CastLambda() {
            public Alert cast(alert alert) {
                return new AnonymousModeAlert(alert.cast_to_anonymous_mode_alert(alert));
            }
        };
        castLambdaArr[60] = new CastLambda() {
            public Alert cast(alert alert) {
                return new LsdPeerAlert(alert.cast_to_lsd_peer_alert(alert));
            }
        };
        castLambdaArr[61] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TrackeridAlert(alert.cast_to_trackerid_alert(alert));
            }
        };
        castLambdaArr[62] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtBootstrapAlert(alert.cast_to_dht_bootstrap_alert(alert));
            }
        };
        castLambdaArr[63] = new CastLambda() {
            public Alert cast(alert alert) {
                return Alerts.handleUnknownAlert(alert);
            }
        };
        castLambdaArr[64] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentErrorAlert(alert.cast_to_torrent_error_alert(alert));
            }
        };
        castLambdaArr[65] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentNeedCertAlert(alert.cast_to_torrent_need_cert_alert(alert));
            }
        };
        castLambdaArr[66] = new CastLambda() {
            public Alert cast(alert alert) {
                return new IncomingConnectionAlert(alert.cast_to_incoming_connection_alert(alert));
            }
        };
        castLambdaArr[67] = new CastLambda() {
            public Alert cast(alert alert) {
                return new AddTorrentAlert(alert.cast_to_add_torrent_alert(alert));
            }
        };
        castLambdaArr[68] = new CastLambda() {
            public Alert cast(alert alert) {
                return new StateUpdateAlert(alert.cast_to_state_update_alert(alert));
            }
        };
        castLambdaArr[69] = new CastLambda() {
            public Alert cast(alert alert) {
                return Alerts.handleUnknownAlert(alert);
            }
        };
        castLambdaArr[70] = new CastLambda() {
            public Alert cast(alert alert) {
                return new SessionStatsAlert(alert.cast_to_session_stats_alert(alert));
            }
        };
        castLambdaArr[71] = new CastLambda() {
            public Alert cast(alert alert) {
                return Alerts.handleUnknownAlert(alert);
            }
        };
        castLambdaArr[72] = new CastLambda() {
            public Alert cast(alert alert) {
                return Alerts.handleUnknownAlert(alert);
            }
        };
        castLambdaArr[73] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtErrorAlert(alert.cast_to_dht_error_alert(alert));
            }
        };
        castLambdaArr[74] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtImmutableItemAlert(alert.cast_to_dht_immutable_item_alert(alert));
            }
        };
        castLambdaArr[75] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtMutableItemAlert(alert.cast_to_dht_mutable_item_alert(alert));
            }
        };
        castLambdaArr[76] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtPutAlert(alert.cast_to_dht_put_alert(alert));
            }
        };
        castLambdaArr[77] = new CastLambda() {
            public Alert cast(alert alert) {
                return new I2pAlert(alert.cast_to_i2p_alert(alert));
            }
        };
        castLambdaArr[78] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtOutgoingGetPeersAlert(alert.cast_to_dht_outgoing_get_peers_alert(alert));
            }
        };
        castLambdaArr[79] = new CastLambda() {
            public Alert cast(alert alert) {
                return new LogAlert(alert.cast_to_log_alert(alert));
            }
        };
        castLambdaArr[80] = new CastLambda() {
            public Alert cast(alert alert) {
                return new TorrentLogAlert(alert.cast_to_torrent_log_alert(alert));
            }
        };
        castLambdaArr[81] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PeerLogAlert(alert.cast_to_peer_log_alert(alert));
            }
        };
        castLambdaArr[82] = new CastLambda() {
            public Alert cast(alert alert) {
                return new LsdErrorAlert(alert.cast_to_lsd_error_alert(alert));
            }
        };
        castLambdaArr[83] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtStatsAlert(alert.cast_to_dht_stats_alert(alert));
            }
        };
        castLambdaArr[84] = new CastLambda() {
            public Alert cast(alert alert) {
                return new IncomingRequestAlert(alert.cast_to_incoming_request_alert(alert));
            }
        };
        castLambdaArr[85] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtLogAlert(alert.cast_to_dht_log_alert(alert));
            }
        };
        castLambdaArr[86] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtPktAlert(alert.cast_to_dht_pkt_alert(alert));
            }
        };
        castLambdaArr[87] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtGetPeersReplyAlert(alert.cast_to_dht_get_peers_reply_alert(alert));
            }
        };
        castLambdaArr[88] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtDirectResponseAlert(alert.cast_to_dht_direct_response_alert(alert));
            }
        };
        castLambdaArr[89] = new CastLambda() {
            public Alert cast(alert alert) {
                return new PickerLogAlert(alert.cast_to_picker_log_alert(alert));
            }
        };
        castLambdaArr[90] = new CastLambda() {
            public Alert cast(alert alert) {
                return new SessionErrorAlert(alert.cast_to_session_error_alert(alert));
            }
        };
        castLambdaArr[91] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtLiveNodesAlert(alert.cast_to_dht_live_nodes_alert(alert));
            }
        };
        castLambdaArr[92] = new CastLambda() {
            public Alert cast(alert alert) {
                return new SessionStatsHeaderAlert(alert.cast_to_session_stats_header_alert(alert));
            }
        };
        castLambdaArr[93] = new CastLambda() {
            public Alert cast(alert alert) {
                return new DhtSampleInfohashesAlert(alert.cast_to_dht_sample_infohashes_alert(alert));
            }
        };
        castLambdaArr[94] = new CastLambda() {
            public Alert cast(alert alert) {
                return new BlockUploadedAlert(alert.cast_to_block_uploaded_alert(alert));
            }
        };
        return castLambdaArr;
    }

    private static Alert handleUnknownAlert(alert alert) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("alert not known: ");
        stringBuilder.append(alert.type());
        stringBuilder.append(" - ");
        stringBuilder.append(alert.message());
        throw new IllegalArgumentException(stringBuilder.toString());
    }
}
